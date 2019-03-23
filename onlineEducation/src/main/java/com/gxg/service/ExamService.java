package com.gxg.service;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 考试相关业务处理接口
 * @author 郭欣光
 * @date 2019/3/21 10:49
 */

public interface ExamService {

    /**
     * 创建考试
     * @param courseId 课程ID
     * @param examName 考试名称
     * @param examRequirement 考试要求
     * @param examStartTime 考试最早考试时间
     * @param examEndTime 考试最晚结束时间
     * @param examDuration 考试时长
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createExam(String courseId, String examName, String examRequirement, String examStartTime, String examEndTime, String examDuration, HttpServletRequest request);

    /**
     * 获取指定课程指定页数的考试信息
     * @param courseId 课程ID
     * @param examPage 页数
     * @return 考试先关信息
     * @author 郭欣光
     */
    JSONObject getExamListByCourseId(String courseId, String examPage);
}
