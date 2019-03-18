package com.gxg.controller;

import com.gxg.entities.*;
import com.gxg.service.*;
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

    @Autowired
    private UserStudyService userStudyService;

    @GetMapping("/my/detail/{videoId}")
    public String videoPage(@PathVariable String videoId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/video/my/detail/" + videoId;
        } else {
            Video video = videoService.getVideoById(videoId);
            if (video == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Lesson lesson = lessonService.getLessonById(video.getLessonId());
            if (lesson == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Course course = courseService.getCourseById(lesson.getCourseId());
            User user = (User)session.getAttribute("user");
            if (course == null || !course.getUserEmail().equals(user.getEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("video", video);
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

    @GetMapping(value = "/get/{lessonId}")
    @ResponseBody
    public String getVideoListByLessonId(@PathVariable String lessonId) {
        return videoService.getVideoListByLessonId(lessonId);
    }

    @GetMapping(value = "/public/detail/{videoId}")
    public String getPublicVideoPage(@PathVariable String videoId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/video/public/detail/" + videoId;
        } else {
            Video video = videoService.getVideoById(videoId);
            if (video == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Lesson lesson = lessonService.getLessonById(video.getLessonId());
            if (lesson == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Course course = courseService.getCourseById(lesson.getCourseId());
            if (course == null || "1".equals(course.getIsPrivate())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("video", video);
            model.addAttribute("lesson", lesson);
            model.addAttribute("course", course);
            User user = (User)session.getAttribute("user");
            userStudyService.addPublicUserStudy(user, course);
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            List<Course> courseListOrderByModifyTime = courseService.getCourseListByIsPrivateAndTopNumberOrderByModifyTime("0", 5);
            model.addAttribute("courseListOrderByModifyTime", courseListOrderByModifyTime);
            if (courseListOrderByModifyTime != null && courseListOrderByModifyTime.size() > 5) {
                model.addAttribute("hasMoreCourseOrderByModifyTime", "yes");
            }
            List<Lesson> lessonList = lessonService.getLessonListByCourseIdAndTopNumber(lesson.getCourseId(), 5);
            model.addAttribute("lessonListOrderByModifyTime", lessonList);
            if (lessonList != null && lessonList.size() >= 5) {
                model.addAttribute("hasMoreLessonOrderByModifyTime", "yes");
            }
            model.addAttribute("courseType", "public");
            return "/video_detail.html";
        }
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String deleteVideo(@RequestParam String videoId, HttpServletRequest request) {
        return videoService.deleteVideo(videoId, request);
    }

    @GetMapping(value = "/private/detail/{videoId}")
    public String getPrivateVideoPage(@PathVariable String videoId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/video/private/detail/" + videoId;
        } else {
            Video video = videoService.getVideoById(videoId);
            if (video == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Lesson lesson = lessonService.getLessonById(video.getLessonId());
            if (lesson == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Course course = courseService.getCourseById(lesson.getCourseId());
            if (course == null || "0".equals(course.getIsPrivate())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            User user = (User)session.getAttribute("user");
            List<UserStudy> userStudyList = userStudyService.getUserStudyByCourseIdAndUserEmail(course.getId(), user.getEmail());
            if (userStudyList == null || userStudyList.size() == 0) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("video", video);
            model.addAttribute("lesson", lesson);
            model.addAttribute("course", course);
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            List<Course> courseListOrderByModifyTime = courseService.getCourseListByIsPrivateAndTopNumberOrderByModifyTime("0", 5);
            model.addAttribute("courseListOrderByModifyTime", courseListOrderByModifyTime);
            if (courseListOrderByModifyTime != null && courseListOrderByModifyTime.size() > 5) {
                model.addAttribute("hasMoreCourseOrderByModifyTime", "yes");
            }
            List<Lesson> lessonList = lessonService.getLessonListByCourseIdAndTopNumber(lesson.getCourseId(), 5);
            model.addAttribute("lessonListOrderByModifyTime", lessonList);
            if (lessonList != null && lessonList.size() >= 5) {
                model.addAttribute("hasMoreLessonOrderByModifyTime", "yes");
            }
            model.addAttribute("courseType", "private");
            return "/video_detail.html";
        }
    }

}
