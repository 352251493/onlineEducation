package com.gxg.controller;

import com.gxg.entities.Discuss;
import com.gxg.entities.User;
import com.gxg.service.CommentService;
import com.gxg.service.DiscussService;
import com.gxg.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired
    private CommentService commentService;

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

    @GetMapping(value = "/create")
    public String getCreateDiscussPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/discuss/create";
        } else {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            return "/create_discuss.html";
        }
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String createDiscuss(@RequestParam String discussName, @RequestParam String discussContent, HttpServletRequest request) {
        return discussService.createDiscuss(discussName, discussContent, request);
    }

    @GetMapping(value = "/detail/{discussId}/{commentPage}")
    public String getDiscussDetailPage(@PathVariable String discussId, @PathVariable String commentPage, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/discuss/detail/" + discussId + "/" + commentPage;
        } else {
            Discuss discuss = discussService.getDiscussById(discussId);
            if (discuss == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            JSONObject commentListInfo = commentService.getCommentListByDiscussId(discussId, commentPage);
            if ("false".equals(commentListInfo.getString("status"))) {
                return "redirect:/discuss/detail/" + discussId + "/1";
            } else {
                int commentPageInt = commentListInfo.getInt("commentPage");
                int commentPageNumber = commentListInfo.getInt("commentPageNumber");
                model.addAttribute("commentPage", commentPageInt);
                model.addAttribute("commentPageNumber", commentPageNumber);
                if (commentPageInt > 1) {
                    int commentPrePage = commentPageInt - 1;
                    model.addAttribute("commentPrePage", commentPrePage);
                }
                if (commentPageInt < commentPageNumber) {
                    int commentNextPage = commentPageInt + 1;
                    model.addAttribute("commentNextPage", commentNextPage);
                }
                if ("true".equals(commentListInfo.getString("hasComment"))) {
                    model.addAttribute("commentList", commentListInfo.get("commentList"));
                }
                model.addAttribute("discuss", discuss);
                User user = (User)session.getAttribute("user");
                int unReadMessageCount = messageService.getUnreadMessageCount(user);
                model.addAttribute("unReadMessageCount", unReadMessageCount);
                model.addAttribute("user", user);
                return "/discuss_detail.html";
            }
        }
    }

    @PostMapping(value = "/comment/create")
    @ResponseBody
    public String createComment(@RequestParam String discussId, @RequestParam String commentContent, HttpServletRequest request) {
        return commentService.createComment(discussId, commentContent, request);
    }

    @PostMapping(value = "/comment/delete")
    @ResponseBody
    public String deleteComment(@RequestParam String commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String deleteDiscuss(@RequestParam String discussId, HttpServletRequest request) {
        return discussService.deleteDiscuss(discussId, request);
    }
}
