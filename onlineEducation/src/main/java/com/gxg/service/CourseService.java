package com.gxg.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 课程相关业务处理
 * @author 郭欣光
 * @date 2019/1/30 14:33
 */

public interface CourseService {

    /**
     * 根据指定页数按照修改时间顺序获取课程列表
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject getCourseListOrderByModifyTime(String coursePage);

    /**
     * 根据指定页数按照学习人数顺序获取课程列表
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject getCourseListOrderByStudyNumber(String coursePage);

    /**
     * 根据搜索内容和指定页数获取课程列表
     * @param searchContent 搜索内容
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject searchCourse(String searchContent, String coursePage);

    /**
     * 创建课程业务逻辑处理
     * @param courseName 课程名称
     * @param courseIntroduction 课程简介
     * @param courseImage 课程封面图片
     * @param isPrivate 是否私有
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createCourse(String courseName, String courseIntroduction, MultipartFile courseImage, String isPrivate, HttpServletRequest request);
}
