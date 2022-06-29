package com.rnginx.test.http.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-06-29 18:51
 **/
@RestController
@RequestMapping("/test")
public class TestController {



    @GetMapping("/w1")
    public String test_w1(){
        return "w1";
    }
}
