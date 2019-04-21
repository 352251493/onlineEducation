package com.gxg.dao;

import com.gxg.entities.StudentObjectiveQuestion;

import java.util.List;

/**
 * 学生客观题数据库相关
 * @author 郭欣光
 * @date 2019/4/17 15:28
 */

public interface StudentObjectiveQuestionDao {

    /**
     * 根据学生考试ID及客观题ID获取学生客观题信息个数
     * @param studentExamId 学生考试ID
     * @param objectiveQuestionId 客观题ID
     * @return 学生客观题信息个数
     */
    int getCountByStudentExamIdAndObjectiveQuestionId(String studentExamId, String objectiveQuestionId);

    /**
     * 根据ID获取学生客观题信息个数
     * @param id ID
     * @return 学生客观题信息个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加学生客观题信息
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int addStudentObjectiveQuestion(StudentObjectiveQuestion studentObjectiveQuestion);

    /**
     * 根据学生考试ID和客观题ID获取学生客观题信息
     * @param studentExamId 学生考试ID
     * @param objectiveQuestionId 客观题ID
     * @return 学生客观题信息
     * @author 郭欣光
     */
    List<StudentObjectiveQuestion> getStudentObjectiveQuestionByStudentExamIdAndObjectiveQuestionId(String studentExamId, String objectiveQuestionId);

    /**
     * 更新学生客观题信息
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateStudentObjectiveQuestion(StudentObjectiveQuestion studentObjectiveQuestion);

    /**
     * 更新成绩
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateScore(StudentObjectiveQuestion studentObjectiveQuestion);

    /**
     * 根据学生考试ID获取学生客观题信息数量
     * @param studentExamId 学生考试ID
     * @return 学生客观题信息数量
     * @author 郭欣光
     */
    int getCountByStudentExamId(String studentExamId);

    /**
     * 根据学生考试ID获取学生客观题信息
     * @param studentExamId 学生考试ID
     * @return 学生客观题信息
     * @author 郭欣光
     */
    List<StudentObjectiveQuestion> getStudentObjectiveQuestionByStudentExamId(String studentExamId);
}
