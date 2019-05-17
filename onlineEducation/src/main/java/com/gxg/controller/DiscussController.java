package com.gxg.controller;

import com.gxg.entities.Course;
import com.gxg.entities.User;
import com.gxg.service.DiscussService;
import com.gxg.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 讨论帖子相关请求与相应控制
 * @author 郭欣光
 * @date 2019/5/17 15:52
 */
@Controller
@RequestMapping(value = "/discuss")
public class DiscussController {

    @Autowired
    private DiscussService discussService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/{page}")
    public String getDiscussList(@PathVariable String page, HttpServletRequest request, Model model) {
        JSONObject discussListInfo = discussService.getDiscussList(page);
        if ("false".equals(discussListInfo.getString("status"))) {
            return "redirect:/discuss/1";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("user") != null) {
                User user = (User)session.getAttribute("user");
                int unReadMessageCount = messageService.getUnreadMessageCount(user);
                model.addAttribute("unReadMessageCount", unReadMessageCount);
                model.addAttribute("user", user);
            }
            int discussPageInt = discussListInfo.getInt("discussPage");
            int discussPageNumber = discussListInfo.getInt("discussPageNumber");
            model.addAttribute("discussPage", discussPageInt);
            model.addAttribute("discussPageNumber", discussPageNumber);
            if (discussPageInt > 1) {
                int discussPrePage = discussPageInt - 1;
                model.addAttribute("discussPrePage", discussPrePage);
            }
            if (discussPageInt < discussPageNumber) {
                int discussNextPage = discussPageInt + 1;
                model.addAttribute("discussNextPage", discussNextPage);
            }
            if ("true".equals(discussListInfo.getString("hasDiscuss"))) {
                model.addAttribute("discussList", discussListInfo.get("discussList"));
            }
            return "/discuss.html";
        }
    }
}
