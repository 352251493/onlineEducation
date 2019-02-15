package com.gxg.controller;

import com.gxg.entities.User;
import com.gxg.service.CourseService;
import com.gxg.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 课程相关请求响应控制
 * @author 郭欣光
 * @date 2019/1/30 10:34
 */

@Controller
@RequestMapping(value = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/time/{page}")
    public String getCountByTime(@PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
        }
        JSONObject courseListInfo = courseService.getCourseListOrderByModifyTime(page);
        if ("false".equals(courseListInfo.getString("status"))) {
            return "redirect:/course/time/1";
        } else {
            int coursePageInt = courseListInfo.getInt("coursePage");
            int coursePageNumber = courseListInfo.getInt("coursePageNumber");
            model.addAttribute("coursePage", coursePageInt);
            model.addAttribute("coursePageNumber", coursePageNumber);
            if (coursePageInt > 1) {
                int coursePrePage = coursePageInt - 1;
                model.addAttribute("coursePrePage", coursePrePage);
            }
            if (coursePageInt < coursePageNumber) {
                int courseNextPage = coursePageInt + 1;
                model.addAttribute("courseNextPage", courseNextPage);
            }
            if ("true".equals(courseListInfo.getString("hasCourse"))) {
                model.addAttribute("courseList", courseListInfo.get("courseList"));
            }
            model.addAttribute("courseListType", "time");
            return "/course.html";
        }
    }

    @GetMapping(value = "/popular/{page}")
    public String getCourseByStudyNumber(@PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
        }
        JSONObject courseListInfo = courseService.getCourseListOrderByStudyNumber(page);
        if ("false".equals(courseListInfo.getString("status"))) {
            return "redirect:/course/popular/1";
        } else {
            int coursePageInt = courseListInfo.getInt("coursePage");
            int coursePageNumber = courseListInfo.getInt("coursePageNumber");
            model.addAttribute("coursePage", coursePageInt);
            model.addAttribute("coursePageNumber", coursePageNumber);
            if (coursePageInt > 1) {
                int coursePrePage = coursePageInt - 1;
                model.addAttribute("coursePrePage", coursePrePage);
            }
            if (coursePageInt < coursePageNumber) {
                int courseNextPage = coursePageInt + 1;
                model.addAttribute("courseNextPage", courseNextPage);
            }
            if ("true".equals(courseListInfo.getString("hasCourse"))) {
                model.addAttribute("courseList", courseListInfo.get("courseList"));
            }
            model.addAttribute("courseListType", "popular");
            return "/course.html";
        }
    }

    @GetMapping(value = "/search/{searchContent}/{page}")
    public String searchCourse(@PathVariable String searchContent, @PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
        }
        JSONObject courseListInfo = courseService.searchCourse(searchContent, page);
        if ("false".equals(courseListInfo.getString("status"))) {
            return "redirect:/course/search/" + searchContent + "/1";
        } else {
            model.addAttribute("searchContent", searchContent);
            int coursePageInt = courseListInfo.getInt("coursePage");
            int coursePageNumber = courseListInfo.getInt("coursePageNumber");
            model.addAttribute("coursePage", coursePageInt);
            model.addAttribute("coursePageNumber", coursePageNumber);
            if (coursePageInt > 1) {
                int coursePrePage = coursePageInt - 1;
                model.addAttribute("coursePrePage", coursePrePage);
            }
            if (coursePageInt < coursePageNumber) {
                int courseNextPage = coursePageInt + 1;
                model.addAttribute("courseNextPage", courseNextPage);
            }
            if ("true".equals(courseListInfo.getString("hasCourse"))) {
                model.addAttribute("courseList", courseListInfo.get("courseList"));
            }
            model.addAttribute("courseListType", "search");
            return "/course.html";
        }
    }

    @GetMapping(value = "/create")
    public String createCoursePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/course/create";
        } else {
            User user = (User)session.getAttribute("user");
            if (!"教师".equals(user.getRole())) {
                model.addAttribute("promptTitle", "权限不足");
                model.addAttribute("promptMessage", "对不起，目前仅有教师才可以创建课程！");
                return "/prompt/prompt.html";
            }
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            return "/create_course.html";
        }
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String createCourse(@RequestParam String courseName, @RequestParam String courseIntroduction, @RequestParam MultipartFile courseImage, @RequestParam String isPrivate, HttpServletRequest request) {
        return courseService.createCourse(courseName, courseIntroduction, courseImage, isPrivate, request);
    }
}
