package com.gxg.dao;

import com.gxg.entities.StudentExam;

import java.util.List;

/**
 * 学生考试相关数据库接口
 * @author 郭欣光
 * @date 2019/4/13 14:56
 */
public interface StudentExamDao {

    /**
     * 根据考试ID和用户邮箱获取用户考试信息个数
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 考试信息个数
     */
    int getCountByExamIdAndUserEmail(String examId, String userEmail);


    /**
     * 根据考试ID及用户邮箱获取学生考试信息
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 学生考试信息
     * @author 郭欣光
     */
    List<StudentExam> getStudentExamByExamIdAndUserEmail(String examId, String userEmail);

    /**
     * 根据ID获取学生考试信息个数
     * @param id ID
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加学生考试信息
     * @param studentExam 学生考试信息
     * @return 数据库改变行数
     */
    int addStudentExam(StudentExam studentExam);

    /**
     * 根据考试ID及用户邮箱更新考试剩余时间
     * @param time 考试剩余时间
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateStudentExamTimeByExamIdAndUserEmail(String time, String examId, String userEmail);

    /**
     * 根据ID获取学生考试信息
     * @param id ID
     * @return 学生考试信息
     * @author 郭欣光
     */
    StudentExam getStudentExamById(String id);

    /**
     * 根据考试ID获取学生考试信息个数
     * @param examId 考试ID
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    int getCountByExamId(String examId);

    /**
     * 根据考试ID按照创建时间排序获取指定位置的学生考试信息
     * @param examId 考试ID
     * @param startLimit 第一个Limit
     * @param endLimit 第二个Limit
     * @return 学生考试信息
     * @author 郭欣光
     */
    List<StudentExam> getStudentExamByExamIdAndLimitOrderByCreateTime(String examId, int startLimit, int endLimit);

    /**
     * 设置成绩
     * @param studentExam 学生考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateScore(StudentExam studentExam);

    /**
     * 根据用户邮箱获取学生考试个数
     * @param userEmail 用户邮箱
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    int getCountByUserEmail(String userEmail);

    /**
     * 根据用户邮箱获取用户考试信息
     * @param userEmail 用户邮箱
     * @return 用户考试信息
     * @author 郭欣光
     */
    List<StudentExam> getStudentExamByUserEmail(String userEmail);

    /**
     * 获取大于等于指定成绩的用户考试信息个数
     * @param score 成绩
     * @return 用户考试信息个数
     * @author 郭欣光
     */
    int getCountGreaterAndEqualsScore(int score);

    /**
     * 获取指定用户邮箱且大于等于指定成绩的用户考试信息个数
     * @param userEmail 用户邮箱
     * @param score 成绩
     * @return 用户考试信息个数
     * @author 郭欣光
     */
    int getCountByUserEmailGreaterAndEqualsScore(String userEmail, int score);

    /**
     * 获取指定用户且大于等于指定成绩按照创建时间排序获取指定范围的用户考试信息
     * @param userEmail 用户邮箱
     * @param score 成绩
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 学生考试信息
     * @author 郭欣光
     */
    List<StudentExam> getStudentExamByUserEmailGreaterAndEqualsScoreAndLimitOrderByCreateTime(String userEmail, int score, int limitStart, int limitEnd);

    /**
     * 获取指定考试ID且大于指定成绩的学生考试信息个数
     * @param examId 考试ID
     * @param score 成绩
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    int getCountByExamIdGreaterScore(String examId, int score);

    /**
     * 根据考试ID获取最高成绩
     * @param examId 考试ID
     * @return 最高成绩
     * @author 郭欣光
     */
    int getMaxScoreByExamId(String examId);

    /**
     * 根据考试ID获取最低成绩
     * @param examId 考试ID
     * @return 最低成绩
     * @author 郭欣光
     */
    int getMinScoreByExamId(String examId);

    /**
     * 根据考试ID获取大于等于指定分数的学生考试个数
     * @param examId 考试ID
     * @param score 成绩
     * @return 学生考试个数
     * @author 郭欣光
     */
    int getCountByExamIdAndGreaterAndEqualsScore(String examId, int score);

    /**
     * 根据考试ID获取大于等于指定分数的平均成绩
     * @param examId 考试ID
     * @param score 成绩
     * @return 评论成绩
     * @author 郭欣光
     */
    double getAvgScoreByExamIdAndGreaterAndEqualsScore(String examId, int score);
}
