package com.gxg.service;

import com.gxg.entities.ObjectiveQuestion;
import com.gxg.entities.StudentExam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学生客观题先关业务处理接口
 * @author 郭欣光
 * @date 2019/4/17 15:14
 */
public interface StudentObjectiveQuestionService {

    /**
     * 设置学生客观题信息答案
     * @param studentExamId 学生考试ID
     * @param objectiveQuestionId 客观题信息ID
     * @param answer 答案
     * @param request 用户请求相关信息
     * @return 处理结果
     */
    String setStudentObjectiveQuestionAnswer(String studentExamId, String objectiveQuestionId, String answer, HttpServletRequest request);

    /**
     * 设置学生客观题答案
     * @param objectiveQuestionList 客观题信息列表
     * @param studentExam 学生考试信息
     * @author 郭欣光
     */
    void setAnswerForObjectiveQuestion(List<ObjectiveQuestion> objectiveQuestionList, StudentExam studentExam);
}
