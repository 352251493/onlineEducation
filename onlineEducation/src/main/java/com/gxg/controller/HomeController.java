package com.gxg.controller;

import com.gxg.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 首页的请求响应控制
 * @author 郭欣光
 * @date 2019/1/14 17:48
 */
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String hone(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        }
        return "/index.html";
    }
}
