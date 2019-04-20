package com.gxg.service;

import com.gxg.entities.ChoiceQuestion;
import com.gxg.entities.StudentExam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学生选择题相关业务接口
 * @author 郭欣光
 * @date 2019/4/14 17:36
 */
public interface StudentChoiceQuestionService {

    /**
     * 设置学生选择题答案
     * @param studentExamId 学生考试ID
     * @param choiceQuestionId 选择题ID
     * @param answer 答案
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String setStudentChoiceQuestionAnswer(String studentExamId, String choiceQuestionId, String answer, HttpServletRequest request);

    /**
     * 将答案赋值给选择题信息
     * @param choiceQuestionList 选择题信息
     * @param studentExam 学生考试信息
     * @return 选择题信息
     * @author 郭欣光
     */
    List<ChoiceQuestion> setAnswerForChoiceQuestion(List<ChoiceQuestion> choiceQuestionList, StudentExam studentExam);

    /**
     * 根据学生考试ID获取选择题成绩
     * @param studentExamId 学生考试ID
     * @return 选择题成绩
     */
    int getChoiceQuestionScoreByStudentExamId(String studentExamId);
}
