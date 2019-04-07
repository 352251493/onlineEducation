package com.gxg.service;

import com.gxg.entities.ChoiceQuestion;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertFalse;
import java.util.List;

/**
 * 选择题先关业务处理
 * @author 郭欣光
 * @date 2019/4/4 15:47
 */
public interface ChoiceQuestionService {

    /**
     * 添加单选题
     * @param examId 考试ID
     * @param subject 题目
     * @param optionA 选项A
     * @param optionB 选项B
     * @param optionC 选项C
     * @param optionD 选项D
     * @param answer 答案
     * @param score 分值
     * @param request 用户请求先关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String addChoiceQuestion(String examId, String subject, String optionA, String optionB, String optionC, String optionD, String answer, String score, HttpServletRequest request);

    /**
     * 根据考试ID获取选择题信息
     * @param examId 考试ID
     * @return 选择题信息
     * @author 郭欣光
     */
    List<ChoiceQuestion> getChoiceQuestionByExamId(String examId);

    /**
     * 删除选择题
     * @param choiceQuestionId 选择题ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteChoiceQuestion(String choiceQuestionId, HttpServletRequest request);
}
