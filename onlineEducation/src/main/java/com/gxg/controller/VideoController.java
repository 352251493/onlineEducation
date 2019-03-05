package com.gxg.controller;

import com.gxg.entities.Course;
import com.gxg.entities.Lesson;
import com.gxg.entities.User;
import com.gxg.service.CourseService;
import com.gxg.service.LessonService;
import com.gxg.service.MessageService;
import com.gxg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 视频相关请求与相应
 * @author 郭欣光
 * @date 2019/3/4 15:04
 */

@Controller
@RequestMapping(value = "/video")
public class VideoController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/my/detail/{lessonId}")
    public String videoPage(@PathVariable String lessonId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/video/my/detail/" + lessonId;
        } else {
            Lesson lesson = lessonService.getLessonById(lessonId);
            Course course = courseService.getCourseById(lesson.getCourseId());
            User user = (User)session.getAttribute("user");
            if (lesson == null || course == null || !course.getUserEmail().equals(user.getEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("lesson", lesson);
            model.addAttribute("course", course);
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            List<Course> courseList = courseService.getUserCourseByTopNumber(user, 5);
            model.addAttribute("myCourseList", courseList);
            if (courseList != null && courseList.size() >= 5) {
                model.addAttribute("hasMoreCourse", "yes");
            }
            List<Lesson> lessonList = lessonService.getLessonListByCourseIdAndTopNumber(lesson.getCourseId(), 5);
            model.addAttribute("myLessonList", lessonList);
            if (lessonList != null && lessonList.size() >= 5) {
                model.addAttribute("hasMoreLesson", "yes");
            }
            return "/my_video_detail.html";
        }
    }

    @GetMapping(value = "/get/number/{lessonId}")
    @ResponseBody
    public String getVideoNumberByLessonId(@PathVariable String lessonId) {
        return videoService.getVideoNumberByLessonId(lessonId);
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String createVideo(@RequestParam String lessonId, @RequestParam String videoName, @RequestParam MultipartFile videoFile, HttpServletRequest request) {
        return videoService.createVideo(lessonId, videoName, videoFile, request);
    }
}
