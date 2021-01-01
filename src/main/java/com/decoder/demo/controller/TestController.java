package com.decoder.demo.controller;

import com.decoder.demo.param.QueryParam;
import com.decoder.demo.vo.StudentVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenhaowen
 * @Description:
 * @date 2021/1/1 下午3:58
 */
@RestController
@RequestMapping("/decode")
public class TestController {

    @PostMapping("/save")
    public String save(@RequestBody QueryParam param) {
        System.out.println(param.toString());
        return "SUCCESS";
    }


    @PostMapping("/query")
    public StudentVO query(@RequestBody QueryParam param) {
        System.out.println(param.toString());
        StudentVO vo = new StudentVO();
        vo.setName("李四");
        vo.setAge(23);
        vo.setAddress("江苏南通");
        return vo;
    }
}
