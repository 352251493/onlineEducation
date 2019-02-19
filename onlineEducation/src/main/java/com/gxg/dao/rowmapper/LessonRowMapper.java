package com.gxg.dao.rowmapper;

import com.gxg.entities.Lesson;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/2/19 10:49
 */
public class LessonRowMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int i) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getString("id"));
        lesson.setName(resultSet.getString("name"));
        lesson.setContent(resultSet.getString("content"));
        lesson.setCreateTime(resultSet.getTimestamp("create_time"));
        lesson.setModifyTime(resultSet.getTimestamp("modify_time"));
        lesson.setCourseId(resultSet.getString("courseId"));
        return lesson;
    }
}
