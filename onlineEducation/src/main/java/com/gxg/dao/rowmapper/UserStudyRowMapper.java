package com.gxg.dao.rowmapper;

import com.gxg.entities.UserStudy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/3/7 14:06
 */
public class UserStudyRowMapper implements RowMapper<UserStudy> {

    @Override
    public UserStudy mapRow(ResultSet resultSet, int i) throws SQLException {
        UserStudy userStudy = new UserStudy();
        userStudy.setId(resultSet.getString("id"));
        userStudy.setUserEmail(resultSet.getString("user_email"));
        userStudy.setCourseId(resultSet.getString("course_id"));
        userStudy.setIsPrivate(resultSet.getString("is_private"));
        userStudy.setCreateTime(resultSet.getTimestamp("create_time"));
        userStudy.setLastStudyTime(resultSet.getTimestamp("last_study_time"));
        return userStudy;
    }
}
