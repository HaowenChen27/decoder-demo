package com.decoder.demo.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenhaowen
 * @Description:
 * @date 2021/1/1 下午2:47
 */
@Data
public class QueryParam implements Serializable {

    private String name;

    private Integer age;

    private String address;


}
