package com.decoder.demo;

import com.alibaba.fastjson.JSONObject;
import com.decoder.demo.param.QueryParam;
import com.decoder.demo.utils.AESUtil;
import com.decoder.demo.utils.RSAUtil;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testAES() {
        QueryParam param = new QueryParam();
        param.setAge(12);
        param.setName("张三");
        param.setAddress("上海张江");
        String dataJson = JSONObject.toJSONString(param);
        String s = AESUtil.encodeAES(dataJson);
        System.out.println("加密：" + s);
        String json = AESUtil.decodeAES(AESUtil.AES_KEY, "123111111111111");
        System.out.println(json);
    }

    @Test
    public void testAESDecode() {
        String s = AESUtil.decodeAES(AESUtil.AES_KEY, "FiYoFMgVer4PkMpYj4wzCPg8a8Vsmn1FMFHdibcuQsKYY8WPa65yab0gDrJdLOVamDF6ESbgEbPz\nWH49rSnapgDdG9/7QqSQvxLzFMEIlBw=");
        System.out.println(s);
    }

    @Test
    public void testRSA() {
        //需要加密文本
        String input = "Hello World!";
        // 初始化
        try {
            Map<String, Object> keyMap = RSAUtil.initKey();
            //获取公钥
            String publicKey = RSAUtil.getPublicKey(keyMap);
            System.out.println("公钥：" + publicKey);
            //获取私钥
            String privateKey = RSAUtil.getPrivateKey(keyMap);
            System.out.println("私钥：" + privateKey);
            String s = RSAUtil.encrypt(input, publicKey);
            System.out.println("加密后：" + s);
            String s2 = RSAUtil.decrypt(s, privateKey);
            System.out.println("解密后：" + s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
