package com.gxg.dao;

import com.gxg.entities.StudentChoiceQuestion;

import java.util.List;

/**
 * 学生选择题数据库相关
 * @author 郭欣光
 * @date 2019/4/14 17:50
 */
public interface StudentChoiceQuestionDao {

    /**
     * 根据学生考试ID及选择题ID获取学生选择题个数
     * @param studentExamId 学生考试ID
     * @param choiceQuestionId 选择题ID
     * @return 学生选择题个数
     * @author 郭欣光
     */
    int getCountByStudentExamIdAndChoiceQuestionId(String studentExamId, String choiceQuestionId);

    /**
     * 根据ID获取学生选择题信息数量
     * @param id ID
     * @return 学生选择题信息个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加学生选择题信息
     * @param studentChoiceQuestion 学生选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int addStudentChoiceQuestion(StudentChoiceQuestion studentChoiceQuestion);

    /**
     * 根据学生考试ID与选择题ID获取学生选择题信息
     * @param studentExamId 学生考试ID
     * @param choiceQuestionId 选择题ID
     * @return 学生选择题信息
     * @author 郭欣光
     */
    List<StudentChoiceQuestion> getStudentChoiceQuestionByStudentExamIdAndChoiceQuestionId(String studentExamId, String choiceQuestionId);

    /**
     * 更新学生选择题信息
     * @param studentChoiceQuestion 学生选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateStudentChoiceQuestion(StudentChoiceQuestion studentChoiceQuestion);
}
