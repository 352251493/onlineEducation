package com.gxg.dao.impl;

import com.gxg.dao.UserDao;
import com.gxg.dao.rowmapper.UserRowMapper;
import com.gxg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2019/1/16 15:28
 */
@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据邮箱获得用户个数
     *
     * @param email 邮箱
     * @return 用户个数
     * @author 郭欣光
     */
    @Override
    public int getUserCountByEmail(String email) {
        String sql = "select count(1) from user where email=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return rowCount;
    }


    /**
     * 向数据库添加用户信息
     *
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createUser(User user) {
        String sql = "insert into user(email, password, name, sex, role, head_image, create_time, is_verification) values(?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getName(), user.getSex(), user.getRole(), user.getHeadImage(), user.getCreateTime(), user.getIsVerification());
        return changeCount;
    }

    /**
     * 删除用户
     *
     * @param email 邮箱
     * @return 数据库改变行数
     */
    @Override
    public int deleteUserByEmail(String email) {
        String sql = "delete from user where email=?";
        int changeCount = jdbcTemplate.update(sql, email);
        return changeCount;
    }

    /**
     * 根据是否验证字段获取用户数量
     *
     * @param isVerification 是否验证
     * @return 用户数量
     */
    @Override
    public int getCountByIsVerification(String isVerification) {
        String sql = "select count(1) from user where is_verification=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, isVerification);
        return rowCount;
    }

    /**
     * 根据是否验证字段获取用户信息
     *
     * @param isVerification 是否验证
     * @return 用户信息
     */
    @Override
    public List<User> getUserByIsVerification(String isVerification) {
        String sql = "select * from user where is_verification=?";
        List<User> userList = jdbcTemplate.query(sql, new UserRowMapper(), isVerification);
        return userList;
    }

    /**
     * 根据邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     * @author 郭欣光
     */
    @Override
    public User getUserByEmail(String email) {
        String sql = "select * from user where email=?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateUser(User user) {
        String sql = "update user set password=?, name=?, sex=?, role=?, head_image=?, create_time=?, is_verification=? where email=?";
        int changeCount = jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getSex(), user.getRole(), user.getHeadImage(), user.getCreateTime(), user.getIsVerification(), user.getEmail());
        return changeCount;
    }

    /**
     * 删除用户
     *
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteUser(User user) {
        String sql = "delete from user where email=?";
        int changeCount = jdbcTemplate.update(sql, user.getEmail());
        return changeCount;
    }
}
