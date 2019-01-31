package com.gxg.service;

import org.json.JSONObject;

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
}
