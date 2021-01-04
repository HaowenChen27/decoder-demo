package com.decoder.demo.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author chenhaowen
 * @Description: AES加解密工具类
 * @date 2021/1/1 下午1:51
 */
public class AESUtil {

    /**
     * 密钥长度16字节，128位
     */
    private static final int AES_KEY_LENGTH = 16;

    /**
     * 算法名字
     */
    private static final String AES_ALGORITHM = "AES";

    /**
     * 算法/模式/填充
     */
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 使用CBC模式，需要一个向量iv，可增加加密算法的强度
     */
    private static final String AES_IV = "0112030445060709";

    /**
     * AES key
     */
    public static final String AES_KEY = "abcdef0123456789";


    private static final String AES_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLOP";

    /**
     * 编码格式
     */
//    private static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * 使用AES加密
     *
     * @param data   被加密的数据
     * @return AES加密后的数据
     */
    public static String encodeAES(String data) {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] text = cipher.doFinal(data.getBytes());
            return new BASE64Encoder().encode(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用AES解密
     *
     * @param aesKey AES Key
     * @param data   被解密的数据
     * @return AES解密后的数据
     */
    public static String decodeAES(String aesKey, String data) {
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), AES_ALGORITHM);
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(data);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(cipher.doFinal(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
