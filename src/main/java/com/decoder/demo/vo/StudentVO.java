package com.decoder.demo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenhaowen
 * @Description:
 * @date 2021/1/1 下午4:32
 */
@Data
public class StudentVO implements Serializable {

    private String name;

    private Integer age;

    private String address;
}
