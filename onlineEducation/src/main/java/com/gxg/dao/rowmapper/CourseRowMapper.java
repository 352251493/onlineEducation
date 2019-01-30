package com.gxg.dao.rowmapper;

import com.gxg.entities.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/1/30 14:56
 */
public class CourseRowMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet resultSet, int i) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getString("id"));
        course.setName(resultSet.getString("name"));
        course.setIntroduction(resultSet.getString("introduction"));
        course.setImage(resultSet.getString("image"));
        course.setStudyNumber(resultSet.getInt("study_number"));
        course.setCreateTime(resultSet.getTimestamp("create_time"));
        course.setModifyTime(resultSet.getTimestamp("modify_time"));
        course.setUserEmail(resultSet.getString("user_email"));
        return course;
    }
}
