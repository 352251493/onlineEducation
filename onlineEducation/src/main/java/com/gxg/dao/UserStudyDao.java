package com.gxg.dao;

import com.gxg.entities.UserStudy;

import java.util.List;

/**
 * 用户学习信息数据库先关处理
 * @author 郭欣光
 * @date 2019/3/7 10:52
 */
public interface UserStudyDao {

    /**
     * 根据用户邮箱及课程ID获取用户学习信息个数
     * @param userEmail 用户邮箱
     * @param courseId 课程ID
     * @return 学习信息个数
     * @author 郭欣光
     */
    int getCountByUserEmailAndCourseId(String userEmail, String courseId);

    /**
     * 根据ID获取用户学习信息个数
     * @param id ID
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加用户学习信息
     * @param userStudy 用户学习信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createUserStudy(UserStudy userStudy);

    /**
     * 根据用户邮箱及课程ID获取用户学习信息
     * @param userEmail 用户邮箱
     * @param courseId 课程ID
     * @return 用户学习信息
     * @author 郭欣光
     */
    List<UserStudy> getUserStudyByUserEmailAndCourseId(String userEmail, String courseId);

    /**
     * 跟新用户学习信息
     * @param userStudy 用户学习信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateUserStudy(UserStudy userStudy);

    /**
     * 根据课程ID获取用户学习信息个数
     * @param courseId 课程ID
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    int getCountByCourseId(String courseId);

    /**
     * 根据用户邮箱获取用户学习信息个数
     * @param userEmail 用户邮箱
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    int getCountByUserEmail(String userEmail);

    /**
     * 据用户邮箱按照最后学习时间获取指定范围的用户学习信息
     * @param userEmail 用户邮箱
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 用户学习信息
     * @author 郭欣光
     */
    List<UserStudy> getUserStudyByUserEmailAndLimitOrderByLastStudyTime(String userEmail, int limitStart, int limitEnd);

    /**
     * 根据用户邮箱及课程类型获取用户学习信息数量
     * @param userEmail 用户邮箱
     * @param isPrivate 课程类型
     * @return 用户学习信息数量
     * @author 郭欣光
     */
    int getCountByUserEmailAndIsPrivate(String userEmail, String isPrivate);

    /**
     * 据用户邮箱及课程类型按照最后学习时间获取指定范围的用户学习信息
     * @param userEmail 用户信息
     * @param isPrivate 课程类型
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 用户学习信息
     * @author 郭欣光
     */
    List<UserStudy> getUserStudyByUserEmailAndIsPrivateAndLimitOrderByLastStudyTime(String userEmail, String isPrivate, int limitStart, int limitEnd);
}
