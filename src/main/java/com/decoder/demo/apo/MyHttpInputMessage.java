package com.decoder.demo.apo;

import com.decoder.demo.utils.AESUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author chenhaowen
 * @Description:
 * @date 2021/1/1 下午3:40
 */
public class MyHttpInputMessage implements HttpInputMessage {

    private HttpHeaders headers;

    private InputStream body;

    public MyHttpInputMessage(HttpInputMessage httpInputMessage) throws IOException {
        this.headers = httpInputMessage.getHeaders();
        this.body = IOUtils.toInputStream(
                Objects.requireNonNull(
                        AESUtil.decodeAES(
                                AESUtil.AES_KEY, IOUtils.toString(httpInputMessage.getBody(), "utf-8")
                        )
                ), "utf-8");
    }


    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
