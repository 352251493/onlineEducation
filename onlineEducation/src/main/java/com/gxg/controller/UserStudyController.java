package com.gxg.controller;

import com.gxg.dao.UserStudyDao;
import com.gxg.entities.Course;
import com.gxg.entities.Lesson;
import com.gxg.entities.User;
import com.gxg.entities.UserStudy;
import com.gxg.service.CourseService;
import com.gxg.service.LessonService;
import com.gxg.service.MessageService;
import com.gxg.service.UserStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 学习人员相关请求与相应
 * @author 郭欣光
 * @date 2019/3/14 19:29
 */

@Controller
@RequestMapping(value = "/study/user")
public class UserStudyController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserStudyService userStudyService;

    @GetMapping(value = "/list/{courseId}")
    public String getStudyUserList(@PathVariable String courseId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/study/user/list/" + courseId;
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseService.getCourseById(courseId);
            if (course == null || !"1".equals(course.getIsPrivate()) || course.getUserEmail() == null || !course.getUserEmail().equals(user.getEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("course", course);
            List<UserStudy> userStudyList = userStudyService.getUserStudyListByCourseId(courseId);
            model.addAttribute("userStudyList", userStudyList);
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            List<Course> courseList = courseService.getUserCourseByTopNumber(user, 5);
            model.addAttribute("myCourseList", courseList);
            if (courseList != null && courseList.size() >= 5) {
                model.addAttribute("hasMoreCourse", "yes");
            }
            List<Lesson> lessonList = lessonService.getLessonListByCourseIdAndTopNumber(courseId, 5);
            model.addAttribute("myLessonList", lessonList);
            if (lessonList != null && lessonList.size() >= 5) {
                model.addAttribute("hasMoreLesson", "yes");
            }
            return "/user_study_manage.html";
        }
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public String addUserStudy(@RequestParam String courseId, @RequestParam String userStudyEmail, HttpServletRequest request) {
        return userStudyService.addUserStudy(courseId, userStudyEmail, request);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String deleteUserStudy(@RequestParam String userStudyId, HttpServletRequest request) {
        return userStudyService.deleteUserStudy(userStudyId, request);
    }
}
