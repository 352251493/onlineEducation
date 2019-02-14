package com.gxg.controller;

import com.gxg.entities.Message;
import com.gxg.entities.User;
import com.gxg.service.MessageService;
import com.gxg.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/login")
    public String getLoginPage(@RequestParam(required = false) String next, Model model, HttpServletRequest request) {
        if (next == null) {
            next = "/";
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            return "redirect:" + next;
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
            return "redirect:/user/login?next=" + "/user/message/" + messageType + "/" + messagePage;
        } else {
            User user = (User)session.getAttribute("user");
            JSONObject messageListInfo = messageService.getMessageList(user, messageType, messagePage);
            if ("false".equals(messageListInfo.getString("status"))) {
                return "redirect:/user/message/all/1";
            } else {
                int messagePageInt = messageListInfo.getInt("messagePage");
                int messagePageNumber = messageListInfo.getInt("messagePageNumber");
                model.addAttribute("messagePage", messagePageInt);
                model.addAttribute("messagePageNumber", messagePageNumber);
                if (messagePageInt > 1) {
                    int messagePrePage = messagePageInt - 1;
                    model.addAttribute("messagePrePage", messagePrePage);
                }
                if (messagePageInt < messagePageNumber) {
                    int messageNextPage = messagePageInt + 1;
                    model.addAttribute("messageNextPage", messageNextPage);
                }
                if ("true".equals(messageListInfo.getString("hasMessage"))) {
                    model.addAttribute("messageList", messageListInfo.get("messageList"));
                }
                model = messageCommonModel(model, user);
                model.addAttribute("messageType", messageType);
                return "/user/message.html";
            }
        }
    }

    @PostMapping(value = "/cancel")
    @ResponseBody
    public String cancel(HttpServletRequest request) {
        return userService.cancel(request);
    }

    @GetMapping(value = "/message/detail/{messageId}")
    public String messageDetail(@PathVariable String messageId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user/message/detail/" + messageId;
        } else {
            User user = (User)session.getAttribute("user");
            Message message = messageService.messageDetail(messageId);
            if (message == null || message.getEmail() == null || !message.getEmail().equals(user.getEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "糟糕，该页面好像不存在！");
                return "/prompt/prompt.html";
            } else {
                model = messageCommonModel(model, user);
                model.addAttribute("message", message);
                return "/user/message_detail.html";
            }
        }
    }

    @PostMapping(value = "/message/delete")
    @ResponseBody
    public String messageDelete(@RequestParam String messageId, HttpServletRequest request) {
        return messageService.deleteMessage(messageId, request);
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PostMapping(value = "/password/reset")
    @ResponseBody
    public String resetPassword(HttpServletRequest request) {
        return userService.sendResetPasswordEmail(request);
    }

    @GetMapping(value = "/password/reset/{email}/{rule}")
    public String resetPasswordPage(@PathVariable String email, @PathVariable String rule, HttpServletRequest request, Model model) {
        JSONObject resetPasswordVerificationResult = userService.resetPasswordVerification(email, rule, request);
        if (resetPasswordVerificationResult.getString("status").equals("true")) {
            return "/user/reset_password.html";
        } else {
            model.addAttribute("promptTitle", "出...出错啦");
            model.addAttribute("promptMessage", resetPasswordVerificationResult.getString("content"));
            return "/prompt/prompt.html";
        }
    }

    @PostMapping(value = "/password/reset/submit")
    @ResponseBody
    public String resetPassword(@RequestParam String password, @RequestParam String repassword, HttpServletRequest request) {
        return userService.resetPassword(password, repassword, request);
    }

    private Model messageCommonModel(Model model, User user) {
        model.addAttribute("user", user);
        int unReadMessageCount = messageService.getUnreadMessageCount(user);
        model.addAttribute("unReadMessageCount", unReadMessageCount);
        return model;
    }
}
