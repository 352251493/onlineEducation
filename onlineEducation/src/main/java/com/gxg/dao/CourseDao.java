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

    /**
     * 获取指定范围按照学习人数排序的课程
     * @param linitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLimitOrderByStudyNumber(int linitStart, int limitEnd);

    /**
     * 获取与课程名称或教师名模糊查询的课程个数
     * @param content 要查找的内容
     * @return 课程个数
     * @author 郭欣光
     */
    int getCountByLikeNameOrLikeUserName(String content);

    /**
     * 获取与课程名称或教师名模糊查询匹配及指定范围按照修改时间排序的课程列表
     * @param content 要查找的内容
     * @param limitStart 第一个limit
     * @param limitStartEnd 第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLikeNameOrLikeUserNameAndLimitOrderByTime(String content, int limitStart, int limitStartEnd);
}
