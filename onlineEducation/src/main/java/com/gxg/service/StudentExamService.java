package com.gxg.service;

import com.gxg.entities.StudentExam;

import javax.servlet.http.HttpServletRequest;

/**
 * 学生考试业务相关业务处理接口
 * @author 郭欣光
 * @date 2019/4/13 14:45
 */
public interface StudentExamService {

    /**
     * 获得学生考试相关信息（如果没有则添加）
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 学生考试信息（如果添加失败则为null）
     * @author 郭欣光
     */
    StudentExam getSetStudentExam(String examId, String userEmail);

    /**
     * 更新考试剩余时间
     * @param examId 考试ID
     * @param examTime 考试剩余时间
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String setStudentExamTime(String examId, String examTime, HttpServletRequest request);
}
