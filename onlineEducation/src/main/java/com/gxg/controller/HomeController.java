package com.gxg.controller;

import com.gxg.entities.Course;
import com.gxg.entities.User;
import com.gxg.service.CourseService;
import com.gxg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 首页的请求响应控制
 * @author 郭欣光
 * @date 2019/1/14 17:48
 */
@Controller
public class HomeController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/")
    public String hone(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
        }
        List<Course> courseListOrderByStudyNumber = courseService.getCourseListByIsPrivateAndTopNumberOrderByStudyNumber("0", 4);
        model.addAttribute("courseListOrderByStudyNumber", courseListOrderByStudyNumber);
        return "/index.html";
    }

    @GetMapping(value = "/about")
    public String aboutUs(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
        }
        return "/about_us.html";
    }
}
