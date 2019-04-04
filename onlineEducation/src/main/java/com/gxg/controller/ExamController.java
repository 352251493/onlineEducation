package com.gxg.controller;

import com.gxg.entities.*;
import com.gxg.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 考试相关请求与相应控制
 * @author 郭欣光
 * @date 2019/3/21 10:46
 */

@Controller
@RequestMapping(value = "/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserStudyService userStudyService;

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @PostMapping(value = "/create")
    @ResponseBody
    public String createExam(@RequestParam String courseId, @RequestParam String examName, @RequestParam String examRequirement, @RequestParam String examStartTime, @RequestParam String examEndTime, @RequestParam String examDuration, HttpServletRequest request) {
        return examService.createExam(courseId, examName, examRequirement, examStartTime, examEndTime, examDuration, request);
    }

    @GetMapping(value = "/my/list/{courseId}/{page}")
    public String myExamListPage(@PathVariable String courseId, @PathVariable String page, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/exam/my/list/" + courseId + "/" + page;
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseService.getCourseById(courseId);
            if (!"教师".equals(user.getRole()) || course == null || !user.getEmail().equals(course.getUserEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            JSONObject examListInfo = examService.getExamListByCourseId(courseId, page);
            if ("false".equals(examListInfo.getString("status"))) {
                return "redirect:/exam/my/list/" + courseId + "/1";
            } else {
                int examPageInt = examListInfo.getInt("examPage");
                int examPageNumber = examListInfo.getInt("examPageNumber");
                model.addAttribute("examPage", examPageInt);
                model.addAttribute("examPageNumber", examPageNumber);
                if (examPageInt > 1) {
                    int examPrePage = examPageInt - 1;
                    model.addAttribute("examPrePage", examPrePage);
                }
                if (examPageInt < examPageNumber) {
                    int examNextPage = examPageInt + 1;
                    model.addAttribute("examNextPage", examNextPage);
                }
                if ("true".equals(examListInfo.getString("hasExam"))) {
                    model.addAttribute("examList", examListInfo.get("examList"));
                }
                model.addAttribute("course", course);
                int unReadMessageCount = messageService.getUnreadMessageCount(user);
                model.addAttribute("unReadMessageCount", unReadMessageCount);
                model.addAttribute("user", user);
                List<Course> courseList = courseService.getUserCourseByTopNumber(user, 5);
                model.addAttribute("myCourseList", courseList);
                if (courseList != null && courseList.size() >= 5) {
                    model.addAttribute("hasMoreCourse", "yes");
                }
            }
            return "/my_exam_list.html";
        }
    }

    @GetMapping(value = "/my/detail/{examId}")
    public String myExamDetailPage(@PathVariable String examId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/exam/my/detail/" + examId;
        } else {
            Exam exam = examService.getExamById(examId);
            if (exam == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            Course course = courseService.getCourseById(exam.getCourseId());
            User user = (User)session.getAttribute("user");
            if (course == null || !course.getUserEmail().equals(user.getEmail())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            model.addAttribute("exam", exam);
            model.addAttribute("course", course);
            int unReadMessageCount = messageService.getUnreadMessageCount(user);
            model.addAttribute("unReadMessageCount", unReadMessageCount);
            model.addAttribute("user", user);
            List<Course> courseList = courseService.getUserCourseByTopNumber(user, 5);
            model.addAttribute("myCourseList", courseList);
            if (courseList != null && courseList.size() >= 5) {
                model.addAttribute("hasMoreCourse", "yes");
            }
            List<Exam> examList = examService.getExamListByCourseIdAndTopNumber(exam.getCourseId(), 5);
            model.addAttribute("myExamList", examList);
            if (examList != null && examList.size() >= 5) {
                model.addAttribute("hasMoreExam", "yes");
            }
            return "/my_exam_detail.html";
        }
    }

    @GetMapping(value = "/public/list/{courseId}/{page}")
    public String getPublicExamListPage(@PathVariable String courseId, @PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/exam/public/list/" + courseId + "/" + page;
        } else {
            Course course = courseService.getCourseById(courseId);
            if (course == null || "1".equals(course.getIsPrivate())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            JSONObject examListInfo = examService.getExamListByCourseId(courseId, page);
            if ("false".equals(examListInfo.getString("status"))) {
                return "redirect:/exam/public/list/" + courseId + "/1";
            } else {
                int examPageInt = examListInfo.getInt("examPage");
                int examPageNumber = examListInfo.getInt("examPageNumber");
                model.addAttribute("examPage", examPageInt);
                model.addAttribute("examPageNumber", examPageNumber);
                if (examPageInt > 1) {
                    int examPrePage = examPageInt - 1;
                    model.addAttribute("examPrePage", examPrePage);
                }
                if (examPageInt < examPageNumber) {
                    int examNextPage = examPageInt + 1;
                    model.addAttribute("examNextPage", examNextPage);
                }
                if ("true".equals(examListInfo.getString("hasExam"))) {
                    model.addAttribute("examList", examListInfo.get("examList"));
                }
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
                List<Course> courseListOrderByStudyNumber = courseService.getCourseListByIsPrivateAndTopNumberOrderByStudyNumber("0", 5);
                model.addAttribute("courseListOrderByStudyNumber", courseListOrderByStudyNumber);
                if (courseListOrderByStudyNumber != null && courseListOrderByStudyNumber.size() > 5) {
                    model.addAttribute("hasMoreCourseOrderByStudyNumber", "yes");
                }
                model.addAttribute("courseType", "public");
            }
        }
        return "/exam_list.html";
    }

    @GetMapping(value = "/private/list/{courseId}/{page}")
    public String getPrivateExamListPage(@PathVariable String courseId, @PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/exam/private/list/" + courseId + "/" + page;
        } else {
            Course course = courseService.getCourseById(courseId);
            if (course == null || "0".equals(course.getIsPrivate())) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            User user = (User)session.getAttribute("user");
            List<UserStudy> userStudyList = userStudyService.getUserStudyByCourseIdAndUserEmail(courseId, user.getEmail());
            if (userStudyList == null || userStudyList.size() == 0) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "对不起，该页面不存在！");
                return "/prompt/prompt.html";
            }
            JSONObject examListInfo = examService.getExamListByCourseId(courseId, page);
            if ("false".equals(examListInfo.getString("status"))) {
                return "redirect:/exam/private/list/" + courseId + "/1";
            } else {
                int examPageInt = examListInfo.getInt("examPage");
                int examPageNumber = examListInfo.getInt("examPageNumber");
                model.addAttribute("examPage", examPageInt);
                model.addAttribute("examPageNumber", examPageNumber);
                if (examPageInt > 1) {
                    int examPrePage = examPageInt - 1;
                    model.addAttribute("examPrePage", examPrePage);
                }
                if (examPageInt < examPageNumber) {
                    int examNextPage = examPageInt + 1;
                    model.addAttribute("examNextPage", examNextPage);
                }
                if ("true".equals(examListInfo.getString("hasExam"))) {
                    model.addAttribute("examList", examListInfo.get("examList"));
                }
                model.addAttribute("course", course);
                int unReadMessageCount = messageService.getUnreadMessageCount(user);
                model.addAttribute("unReadMessageCount", unReadMessageCount);
                model.addAttribute("user", user);
                List<Course> courseListOrderByModifyTime = courseService.getCourseListByIsPrivateAndTopNumberOrderByModifyTime("0", 5);
                model.addAttribute("courseListOrderByModifyTime", courseListOrderByModifyTime);
                if (courseListOrderByModifyTime != null && courseListOrderByModifyTime.size() > 5) {
                    model.addAttribute("hasMoreCourseOrderByModifyTime", "yes");
                }
                List<Course> courseListOrderByStudyNumber = courseService.getCourseListByIsPrivateAndTopNumberOrderByStudyNumber("0", 5);
                model.addAttribute("courseListOrderByStudyNumber", courseListOrderByStudyNumber);
                if (courseListOrderByStudyNumber != null && courseListOrderByStudyNumber.size() > 5) {
                    model.addAttribute("hasMoreCourseOrderByStudyNumber", "yes");
                }
                model.addAttribute("courseType", "private");
            }
            return "/exam_list.html";
        }
    }

    @PostMapping(value = "/add/choice")
    @ResponseBody
    public String addChoiceQuestion(@RequestParam String examId, @RequestParam String subject, @RequestParam String optionA, @RequestParam String optionB, @RequestParam String optionC, @RequestParam String optionD, @RequestParam String answer, HttpServletRequest request) {
        return choiceQuestionService.addChoiceQuestion(examId, subject, optionA, optionB, optionC, optionD, answer, request);
    }
}
