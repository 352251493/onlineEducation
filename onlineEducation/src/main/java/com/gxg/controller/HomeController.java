package com.gxg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页的请求响应控制
 * @author 郭欣光
 * @date 2019/1/14 17:48
 */
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String hone() {
        return "/index.html";
    }
}
