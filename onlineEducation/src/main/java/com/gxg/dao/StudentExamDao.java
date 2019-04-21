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

}
