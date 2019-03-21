package com.gxg.controller;

import com.gxg.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping(value = "/create")
    @ResponseBody
    public String createExam(@RequestParam String courseId, @RequestParam String examName, @RequestParam String examRequirement, @RequestParam String examStartTime, @RequestParam String examEndTime, @RequestParam String examDuration, HttpServletRequest request) {
        return examService.createExam(courseId, examName, examRequirement, examStartTime, examEndTime, examDuration, request);
    }
}
