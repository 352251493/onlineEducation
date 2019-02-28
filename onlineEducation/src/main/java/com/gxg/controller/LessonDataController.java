package com.gxg.controller;

import com.gxg.service.LessonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 课时资料相关请求与控制
 * @author 郭欣光
 * @date 2019/2/27 11:17
 */
@Controller
@RequestMapping(value = "/data")
public class LessonDataController {


    @Autowired
    private LessonDataService lessonDataService;

    @GetMapping(value = "/get/number/{lessonId}")
    @ResponseBody
    public String getNumberByLessonId(@PathVariable String lessonId) {
        return lessonDataService.getLessonDataNumberByLessonId(lessonId);
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public String createLessonData(@RequestParam String lessonId, @RequestParam MultipartFile lessonDataFile, HttpServletRequest request) {
        return lessonDataService.createLessonDate(lessonId, lessonDataFile, request);
    }
}
