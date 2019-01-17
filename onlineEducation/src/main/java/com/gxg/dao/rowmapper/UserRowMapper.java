package com.gxg.dao.rowmapper;

import com.gxg.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/1/17 10:22
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setSex(resultSet.getString("sex"));
        user.setRole(resultSet.getString("role"));
        user.setHeadImage(resultSet.getString("head_image"));
        user.setCreateTime(resultSet.getTimestamp("create_time"));
        user.setIsVerification(resultSet.getString("is_verification"));
        return user;
    }
}
