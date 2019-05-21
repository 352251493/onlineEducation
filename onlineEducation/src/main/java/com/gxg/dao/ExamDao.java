package com.gxg.dao;

import com.gxg.entities.Exam;

import java.util.List;

/**
 * 考试相关数据库信息
 * @author 郭欣光
 * @date 2019/3/21 11:21
 */
public interface ExamDao {

    /**
     * 通过ID获取考试数量
     * @param id ID
     * @return 考试数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加考试
     * @param exam 考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createExam(Exam exam);

    /**
     * 根据课程ID获取考试信息个数
     * @param courseId 课程ID
     * @return 考试信息个数
     * @author 郭欣光
     */
    int getCountByCourseId(String courseId);

    /**
     * 根据课程ID按照修改时间获取指定页面的考试信息
     * @param courseId 课程ID
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 考试信息
     * @author 郭欣光
     */
    List<Exam> getExamByCourseIdAndLimitOrderByModifyTime(String courseId, int limitStart, int limitEnd);

    /**
     * 根据ID获取考试信息
     * @param id 考试ID
     * @return 考试信息
     * @author 郭欣光
     */
    Exam getExamById(String id);

    /**
     * 修改考试信息
     * @param exam 考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateExam(Exam exam);

    /**
     * 删除考试
     * @param exam 考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteExam(Exam exam);

    /**
     * 根据考试所属课程用户邮箱获取考试个数
     * @param userEmail 用户邮箱
     * @return 考试个数
     * @author 郭欣光
     */
    int getCountByCourseUserEmail(String userEmail);

    /**
     * 根据考试所属课程用户邮箱按照修改时间排序获取指定范围的考试信息
     * @param userEmail 用户邮箱
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 考试信息
     * @author 郭欣光
     */
    List<Exam> getExamByCourseUserEmailAndLimitOrderByModifyTiime(String userEmail, int limitStart, int limitEnd);
}
