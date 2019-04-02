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
}
