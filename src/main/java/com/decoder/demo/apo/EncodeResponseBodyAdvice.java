package com.decoder.demo.apo;

import com.decoder.demo.utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author chenhaowen
 * @Description:
 * @date 2021/1/1 下午4:40
 */
@ControllerAdvice(basePackages = {"com.decoder.demo.controller"})
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {

    private static final Logger logger= LoggerFactory.getLogger("EncodeResponseBodyAdvice");

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            return AESUtil.encodeAES(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
