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

    /**
     * 根据是否私有获取课程个数
     * @param isPrivate 是否私有
     * @return 课程个数
     * @author 郭欣光
     */
    int getCourseCountByIsPrivate(String isPrivate);

    /**
     * 根据是否私有获取指定范围按照修改时间排序的课程
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @param isPrivate 是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLimitAndIsPrivateOrderByModifyTime(int limitStart, int limitEnd, String isPrivate);

    /**
     * 根据是否私有获取指定范围按照学习人数排序的课程
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @param isPrivate 是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLimitAndIsPrivateOrderByStudyNumber(int limitStart, int limitEnd, String isPrivate);

    /**
     * 根据是否私有获取与课程名称或教师名模糊查询的课程个数
     * @param content 要查找的内容
     * @param isPrivate 是否私有
     * @return 课程个数
     * @author 郭欣光
     */
    int getCountByLikeNameOrLikeUserNameAndIsPrivate(String content, String isPrivate);

    /**
     * 获取与指定是否私有、课程名称或教师名模糊查询匹配及指定范围按照修改时间排序的课程列表
     * @param content 要查找的内容
     * @param limitStart 第一个limit
     * @param limitStartEnd 第二个limit
     * @param isPrivate 是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByLikeNameOrLikeUserNameAndLimitAndIsPrivateOrderByTime(String content, int limitStart, int limitStartEnd, String isPrivate);

    /**
     * 根据ID获取课程数量
     * @param id ID
     * @return 课程数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加课程信息
     * @param course 课程信息
     * @return 操作数据库行数
     * @author 郭欣光
     */
    int createCourse(Course course);

    /**
     * 获取指定用户的课程个数
     * @param userEmail 用户邮箱
     * @return 课程个数
     * @author 郭欣光
     */
    int getCourseCountByUserEmail(String userEmail);

    /**
     * 获取指定用户指定范围的课程
     * @param userEmail 用户邮箱
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseByUserEmailAndLimit(String userEmail, int limitStart, int limitEnd);

    /**
     * 根据ID获取课程信息
     * @param id ID
     * @return 课程信息
     * @author 郭欣光
     */
    Course getCourseById(String id);
}
