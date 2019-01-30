package com.gxg.dao;

import com.gxg.entities.Course;

import java.util.List;

/**
 * 课程相关数据库接口
 * @author 郭欣光
 * @date 2019/1/30 14:43
 */
public interface CourseDao {

    /**
     * 获得课程数量
     * @return 课程数量
     * @author 郭欣光
     */
    int getCourseCount();

    /**
     * 获取指定范围按照修改时间排序的课程
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLimitOrderByModifyTime(int limitStart, int limitEnd);
}
