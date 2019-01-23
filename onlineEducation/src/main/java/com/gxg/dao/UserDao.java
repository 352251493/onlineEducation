package com.gxg.dao;

import com.gxg.entities.User;

import java.util.List;

/**
 * 用户操作数据库相关接口
 * @author 郭欣光
 * @date 2019/1/16 15:27
 */
public interface UserDao {

    /**
     * 根据邮箱获得用户个数
     * @param email 邮箱
     * @return 用户个数
     * @author 郭欣光
     */
    int getUserCountByEmail(String email);

    /**
     * 向数据库添加用户信息
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createUser(User user);

    /**
     * 删除用户
     * @param email 邮箱
     * @return 数据库改变行数
     */
    int deleteUserByEmail(String email);

    /**
     * 根据是否验证字段获取用户数量
     * @param isVerification 是否验证
     * @return 用户数量
     */
    int getCountByIsVerification(String isVerification);

    /**
     * 根据是否验证字段获取用户信息
     * @param isVerification 是否验证
     * @return 用户信息
     */
    List<User> getUserByIsVerification(String isVerification);

    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     * @author 郭欣光
     */
    User getUserByEmail(String email);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int updateUser(User user);

    /**
     * 删除用户
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteUser(User user);
}
