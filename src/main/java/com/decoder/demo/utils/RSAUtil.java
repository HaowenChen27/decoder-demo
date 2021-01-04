package com.decoder.demo.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhaowen
 * @Description: RSA 加密工具类
 * @date 2021/1/4 上午9:48
 */
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String SIGNATURE_ALGORITHM="MD5withRSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获得公钥字符串
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }


    /**
     * 获得私钥字符串
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 生成公钥
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey generatePublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 生成私钥
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey generatePrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 解码返回byte
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }


    /**
     * 编码返回字符串
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * 签名
     * @param data
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] data,String privateKeyStr) throws Exception{
        PrivateKey priK = generatePrivateKey(privateKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(priK);
        sig.update(data);
        return sig.sign();
    }

    /**
     * 验签
     * @param data
     * @param sign
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,byte[] sign,String publicKeyStr) throws Exception{
        PublicKey pubK = generatePublicKey(publicKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(pubK);
        sig.update(data);
        return sig.verify(sign);
    }


    /**
     * 加密
     * @param plainText
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] plainText,String publicKeyStr)throws Exception{
        PublicKey publicKey = generatePublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return fragmentHandle(plainText, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 使用Base64返回加密的字符串
     * @param text
     * @param publicKeyStr
     * @return
     */
    public static String encrypt(String text, String publicKeyStr) {
        try {
            byte[] encrypt = encrypt(text.getBytes(), publicKeyStr);
            return encryptBASE64(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param encryptText
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptText,String privateKeyStr)throws Exception{
        PrivateKey privateKey = generatePrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return fragmentHandle(encryptText, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 使用Base64返回解密的字符串
     * @param text
     * @param privateKeyStr
     * @return
     */
    public static String decrypt(String text, String privateKeyStr) {
        try {
            byte[] bytes = decryptBASE64(text);
            byte[] decrypt = decrypt(bytes, privateKeyStr);
            return new String(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对数据分段加解密
     * @param text
     * @param cipher
     * @param maxBlock
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    private static byte[] fragmentHandle(byte[] text, Cipher cipher, int maxBlock) throws IllegalBlockSizeException, BadPaddingException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = text.length;
        int offSet = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(text, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(text, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        byte[] plainText = out.toByteArray();
        out.close();
        return plainText;
    }

    /**
     * 初始化公钥秘钥
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

}
