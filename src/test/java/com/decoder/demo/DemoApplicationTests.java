package com.decoder.demo;

import com.alibaba.fastjson.JSONObject;
import com.decoder.demo.param.QueryParam;
import com.decoder.demo.utils.AESUtil;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
