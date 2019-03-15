package com.gxg.dao.impl;

import com.gxg.dao.UserStudyDao;
import com.gxg.dao.rowmapper.UserStudyRowMapper;
import com.gxg.entities.UserStudy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户学习信息数据库相关
 * @author 郭欣光
 * @date 2019/3/7 10:53
 */

@Repository(value = "userStudyDao")
public class UserStudyDaoImpl implements UserStudyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户邮箱及课程ID获取用户学习信息个数
     *
     * @param userEmail 用户邮箱
     * @param courseId  课程ID
     * @return 学习信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountByUserEmailAndCourseId(String userEmail, String courseId) {
        String sql = "select count(1) from user_study where user_email=? and course_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userEmail, courseId);
        return rowCount;
    }

    /**
     * 根据ID获取用户学习信息个数
     *
     * @param id ID
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from user_study where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加用户学习信息
     *
     * @param userStudy 用户学习信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createUserStudy(UserStudy userStudy) {
        String sql = "insert into user_study(id, user_email, course_id, is_private, create_time, last_study_time) values(?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, userStudy.getId(), userStudy.getUserEmail(), userStudy.getCourseId(), userStudy.getIsPrivate(), userStudy.getCreateTime(), userStudy.getLastStudyTime());
        return changeCount;
    }

    /**
     * 根据用户邮箱及课程ID获取用户学习信息
     *
     * @param userEmail 用户邮箱
     * @param courseId  课程ID
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyByUserEmailAndCourseId(String userEmail, String courseId) {
        String sql = "select * from user_study where user_email=? and course_id=?";
        List<UserStudy> userStudyList= jdbcTemplate.query(sql, new UserStudyRowMapper(), userEmail, courseId);
        return userStudyList;
    }

    /**
     * 跟新用户学习信息
     *
     * @param userStudy 用户学习信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateUserStudy(UserStudy userStudy) {
        String sql = "update user_study set user_email=?, course_id=?, is_private=?, create_time=?, last_study_time=? where id=?";
        int changeCount = jdbcTemplate.update(sql, userStudy.getUserEmail(), userStudy.getCourseId(), userStudy.getIsPrivate(), userStudy.getCreateTime(), userStudy.getLastStudyTime(), userStudy.getId());
        return changeCount;
    }

    /**
     * 根据课程ID获取用户学习信息个数
     *
     * @param courseId 课程ID
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountByCourseId(String courseId) {
        String sql = "select count(1) from user_study where course_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, courseId);
        return rowCount;
    }

    /**
     * 根据用户邮箱获取用户学习信息个数
     *
     * @param userEmail 用户邮箱
     * @return 用户学习信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountByUserEmail(String userEmail) {
        String sql = "select count(1) from user_study where user_email=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userEmail);
        return rowCount;
    }

    /**
     * 根据用户邮箱按照最后学习时间获取指定范围的用户学习信息
     *
     * @param userEmail  用户邮箱
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyByUserEmailAndLimitOrderByLastStudyTime(String userEmail, int limitStart, int limitEnd) {
        String sql = "select * from user_study where user_email=? order by last_study_time desc where limit ?, ?";
        List<UserStudy> userStudyList = jdbcTemplate.query(sql, new UserStudyRowMapper(), userEmail, limitStart, limitEnd);
        return userStudyList;
    }


    /**
     * 根据用户邮箱及课程类型获取用户学习信息数量
     *
     * @param userEmail 用户邮箱
     * @param isPrivate 课程类型
     * @return 用户学习信息数量
     * @author 郭欣光
     */
    @Override
    public int getCountByUserEmailAndIsPrivate(String userEmail, String isPrivate) {
        String sql = "select count(1) from user_study where user_email=? and is_private=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userEmail, isPrivate);
        return rowCount;
    }

    /**
     * 据用户邮箱及课程类型按照最后学习时间获取指定范围的用户学习信息
     *
     * @param userEmail  用户信息
     * @param isPrivate  课程类型
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyByUserEmailAndIsPrivateAndLimitOrderByLastStudyTime(String userEmail, String isPrivate, int limitStart, int limitEnd) {
        String sql = "select * from user_study where user_email=? and is_private=? order by last_study_time desc limit ?, ?";
        List<UserStudy> userStudyList = jdbcTemplate.query(sql, new UserStudyRowMapper(), userEmail, isPrivate, limitStart, limitEnd);
        return userStudyList;
    }

    /**
     * 根据课程ID按照创建时间排序获取用户学习信息
     *
     * @param courseId 课程ID
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyByCourseIdOrderByCreateTime(String courseId) {
        String sql = "select * from user_study where course_id=? order by create_time desc";
        List<UserStudy> userStudyList = jdbcTemplate.query(sql, new UserStudyRowMapper(), courseId);
        return userStudyList;
    }
}
