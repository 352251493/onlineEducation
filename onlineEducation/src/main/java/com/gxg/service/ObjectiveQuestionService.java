package com.gxg.service;

import com.gxg.entities.ObjectiveQuestion;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 问答题相关业务处理
 * @author 郭欣光
 * @date 2019/4/5 18:17
 */
public interface ObjectiveQuestionService {

    /**
     * 添加问答题
     * @param examId 考试ID
     * @param subject 题目
     * @param score 分值
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String addObjectQuestion(String examId, String subject, String score, HttpServletRequest request);

    /**
     * 根据考试ID获取客观题信息
     * @param examId 考试ID
     * @return 客观题信息
     * @author 郭欣光
     */
    List<ObjectiveQuestion> getObjectQuestionByExamId(String examId);

    /**
     * 根据客观题ID删除客观题
     * @param objectiveQuestionId 客观题ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteObjectiveQuestion(String objectiveQuestionId, HttpServletRequest request);
}
