package com.gxg.dao;

import com.gxg.entities.Lesson;

import java.util.List;

/**
 * 课时相关数据库接口
 * @author 郭欣光
 * @date 2019/2/19 10:33
 */
public interface LessonDao {

    /**
     * 根据课程ID获取课时个数
     * @param courseId 课程ID
     * @return 课时个数
     * @author 郭欣光
     */
    int getCountByCourseId(String courseId);

    /**
     * 根据课程ID按照修改时间获取指定范围的课时信息
     * @param courseId 课程ID
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 课时信息
     * @author 郭欣光
     */
    List<Lesson> getLessonByCourseIdAndLimitOrderByModifyTime(String courseId, int limitStart, int limitEnd);

    /**
     * 根据ID获取课时数量
     * @param id 课时ID
     * @return 课时数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 创建课时
     * @param lesson 课时信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createLesson(Lesson lesson);

    /**
     * 根据课时ID获取课时信息
     * @param id 课时ID
     * @return 课时信息
     * @author 郭欣光
     */
    Lesson getLessonById(String id);

    /**
     * 修改课时信息
     * @param lesson 课时信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int editLesson(Lesson lesson);

    /**
     * 删除课时信息
     * @param lesson 课时信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteLesson(Lesson lesson);
}
