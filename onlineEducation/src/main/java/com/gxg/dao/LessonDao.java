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
}
