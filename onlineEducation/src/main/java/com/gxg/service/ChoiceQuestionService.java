package com.gxg.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 选择题先关业务处理
 * @author 郭欣光
 * @date 2019/4/4 15:47
 */
public interface ChoiceQuestionService {

    String addChoiceQuestion(String examId, String subject, String optionA, String optionB, String optionC, String optionD, String answer, HttpServletRequest request);
}
