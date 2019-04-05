package com.gxg.service;

import javax.servlet.http.HttpServletRequest;

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
}
