package com.gxg.dao.rowmapper;

import com.gxg.entities.LessonData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/3/1 10:54
 */
public class LessonDataRowMapper implements RowMapper<LessonData> {

    @Override
    public LessonData mapRow(ResultSet resultSet, int i) throws SQLException {
        LessonData lessonData = new LessonData();
        lessonData.setId(resultSet.getString("id"));
        lessonData.setName(resultSet.getString("name"));
        lessonData.setPath(resultSet.getString("path"));
        lessonData.setCreateTime(resultSet.getTimestamp("create_time"));
        lessonData.setLessonId(resultSet.getString("lesson_id"));
        return lessonData;
    }
}
