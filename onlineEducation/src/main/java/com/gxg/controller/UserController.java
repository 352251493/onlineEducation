package com.gxg.controller;

import com.gxg.entities.Message;
import com.gxg.entities.User;
import com.gxg.service.*;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserStudyService userStudyService;

    @Autowired
    private StudentExamService studentExamService;

    @Autowired
    private DiscussService discussService;

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
                model.addAttribute("pageName", "消息通知");
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
                model.addAttribute("pageName", "消息通知");
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

    @GetMapping("/course/{myCourseType}/{page}")
    public String myCourse(@PathVariable String myCourseType, @PathVariable String page, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user/course/" + myCourseType + "/" + page;
        } else {
            User user = (User)session.getAttribute("user");
            JSONObject courseListInfo = null;
            if ("create".equals(myCourseType)) {
                courseListInfo = courseService.getCourseByUser(user, page);
                model.addAttribute("pageName", "我创建的");
            } else if ("public".equals(myCourseType)) {
                courseListInfo = userStudyService.getStudyCourseByUserAndIsPrivate(user, "0", page);
                model.addAttribute("pageName", "公共课程");
            } else if ("private".equals(myCourseType)) {
                courseListInfo = userStudyService.getStudyCourseByUserAndIsPrivate(user, "1", page);
                model.addAttribute("pageName", "私有课程");
            }
            if (courseListInfo == null) {
                model.addAttribute("promptTitle", "404");
                model.addAttribute("promptMessage", "糟糕，该页面好像不存在！");
                return "/prompt/prompt.html";
            } else {
                if ("false".equals(courseListInfo.getString("status"))) {
                    return "redirect:/user/course/create/1";
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
                    model = messageCommonModel(model, user);
                }
            }
            return "/user/my_course.html";
        }
    }

    @PostMapping(value = "/get")
    @ResponseBody
    public String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "No user login";
        } else {
            User user = (User)session.getAttribute("user");
            return user.toString();
        }
    }

    @GetMapping(value = "")
    public String getUserHomePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user";
        } else {
            User user = (User)session.getAttribute("user");
            model = messageCommonModel(model, user);
            model.addAttribute("pageName", "个人中心");
            int studyCourseCount = userStudyService.getUserStudyCountByUserEmail(user.getEmail());
            model.addAttribute("studyCourseCount", studyCourseCount);
            int myCourseCount = courseService.getCourseCountByUserEmail(user.getEmail());
            model.addAttribute("myCourseCount", myCourseCount);
            int myExamCount = studentExamService.getStudentExamCountByUserEmail(user.getEmail());
            model.addAttribute("myExamCount", myExamCount);
            String averageScore = studentExamService.getAverageScoreByUserEmail(user.getEmail());
            model.addAttribute("averageScore", averageScore);
            int excellentCount = studentExamService.getStudentExamCountByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 90);
            int goodCount = studentExamService.getStudentExamCountByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 80) - excellentCount;
            int secondaryCount = studentExamService.getStudentExamCountByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 70) - goodCount - excellentCount;
            int passCount = studentExamService.getStudentExamCountByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 60) - secondaryCount - goodCount - excellentCount;
            int failCount = studentExamService.getStudentExamCountByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 0) - passCount - secondaryCount - goodCount - excellentCount;
            model.addAttribute("excellentCount", excellentCount);
            model.addAttribute("goodCount", goodCount);
            model.addAttribute("secondaryCount", secondaryCount);
            model.addAttribute("passCount", passCount);
            model.addAttribute("failCount", failCount);
            String excellentProportion = studentExamService.getStudentExamCountProportionByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 90);
            String goodProportion = studentExamService.getStudentExamCountProportionByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 80);
            String secondaryProportion = studentExamService.getStudentExamCountProportionByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 70);
            String passProportion = studentExamService.getStudentExamCountProportionByUserEmailAndGreaterAndEqualsScore(user.getEmail(), 60);
            String failProportion = studentExamService.getFailStudentExamCount(user.getEmail());
            model.addAttribute("excellentProportion", excellentProportion);
            model.addAttribute("goodProportion", goodProportion);
            model.addAttribute("secondaryProportion", secondaryProportion);
            model.addAttribute("passProportion", passProportion);
            model.addAttribute("failProportion", failProportion);
            return "/user/index.html";
        }
    }

    @GetMapping(value = "/score/list/{scorePage}")
    public String myScoreListPage(@PathVariable String scorePage, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user/score/list/" + scorePage;
        } else {
            User user = (User)session.getAttribute("user");
            JSONObject studentExamListInfo = studentExamService.getMyStudentExamList(user, scorePage);
            if ("false".equals(studentExamListInfo.getString("status"))) {
                return "redirect:/user/score/list/1";
            } else {
                int scorePageInt = studentExamListInfo.getInt("scorePage");
                int scorePageNumber = studentExamListInfo.getInt("scorePageNumber");
                model.addAttribute("scorePage", scorePageInt);
                model.addAttribute("scorePageNumber", scorePageNumber);
                if (scorePageInt > 1) {
                    int scorePrePage = scorePageInt - 1;
                    model.addAttribute("scorePrePage", scorePrePage);
                }
                if (scorePageInt < scorePageNumber) {
                    int scoreNextPage = scorePageInt + 1;
                    model.addAttribute("scoreNextPage", scoreNextPage);
                }
                if ("true".equals(studentExamListInfo.getString("hasStudentExam"))) {
                    model.addAttribute("studentExamList", studentExamListInfo.get("studentExamList"));
                }
                model = messageCommonModel(model, user);
                model.addAttribute("pageName", "考试列表");
                return "/user/score.html";
            }
        }
    }

    @GetMapping(value = "/discuss/list/{discussPage}")
    public String getDiscussListPage(@PathVariable String discussPage, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login?next=" + "/user/discuss/list/" + discussPage;
        } else {
            User user = (User)session.getAttribute("user");
            JSONObject discussListInfo = discussService.getDiscussListByUserEmail(user.getEmail(), discussPage);
            if ("false".equals(discussListInfo.getString("status"))) {
                return "redirect:/user/discuss/list/1";
            } else {
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
                model = messageCommonModel(model, user);
                model.addAttribute("pageName", "我的讨论");
            }
            return "/user/my_discuss.html";
        }
    }
}
