package com.gxg.controller;

import com.gxg.entities.User;
import com.gxg.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 该类为用户相关的请求响应控制类
 * @author 郭欣光
 * @date 2019/1/15 10:33
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String getLoginPage(@RequestParam(required = false) String next, Model model) {
        if (next == null) {
            next = "/";
        }
        model.addAttribute("nextPage", next);
        return "/user/login.html";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public String login(@RequestParam String email, @RequestParam String password, @RequestParam String nextPage, HttpServletRequest request) {
        return userService.login(email, password, nextPage, request);
    }

    @GetMapping(value = "/register")
    public String getRegisterPage() {
        return "/user/register.html";
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public String register(@RequestParam String email, @RequestParam String name, @RequestParam String password, @RequestParam String repassword, @RequestParam String sex, @RequestParam String job, @RequestParam MultipartFile headImage, HttpServletRequest request) {
        return userService.register(email, name, password, repassword, sex, job, headImage, request);
    }

    @GetMapping(value = "/register/{email}/{verificationRule}")
    public String register(@PathVariable String email, @PathVariable String verificationRule, Model model) {
        JSONObject result = userService.register(email, verificationRule);
        model.addAttribute("promptTitle", result.getString("promptTitle"));
        model.addAttribute("promptMessage", result.getString("promptMessage"));
        return "/prompt/prompt.html";
    }

    @GetMapping(value = "/message/{messageType}/{messagePage}")
    public String message(@PathVariable String messageType, @PathVariable String messagePage, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user/message";
        } else {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
            model.addAttribute("mesageType", messagePage);
            model.addAttribute("messagePage", messagePage);
            return "/user/message.html";
        }
    }
}
