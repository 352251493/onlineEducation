package com.gxg.service;

import com.gxg.entities.StudentExam;
import org.json.JSONObject;

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

    /**
     * 获取指定考试ID指定页数的学生考试信息
     * @param examId 考试ID
     * @param page 页数
     * @return 学生考试信息
     * @author 郭欣光
     */
    JSONObject getStudentExamListByExamId(String examId, String page);

    /**
     * 根据学生考试ID获取学生考试信息
     * @param studentExamId 学生考试ID
     * @return 学生考试信息
     */
    StudentExam getStudentExamById(String studentExamId);

    /**
     * 计算学生考试成绩
     * @param studentExamId 学生考试ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String setStudentExamScore(String studentExamId, HttpServletRequest request);
}
