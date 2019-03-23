package com.gxg.dao.rowmapper;

import com.gxg.entities.Exam;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/3/23 11:53
 */
public class ExamRowMapper implements RowMapper<Exam> {

    @Override
    public Exam mapRow(ResultSet resultSet, int i) throws SQLException {
        Exam exam = new Exam();
        exam.setId(resultSet.getString("id"));
        exam.setName(resultSet.getString("name"));
        exam.setRequirement(resultSet.getString("requirement"));
        exam.setStartTime(resultSet.getTimestamp("start_time"));
        exam.setEndTime(resultSet.getTimestamp("end_time"));
        exam.setDuration(resultSet.getInt("duration"));
        exam.setCreateTime(resultSet.getTimestamp("create_time"));
        exam.setModifyTime(resultSet.getTimestamp("modify_time"));
        exam.setCourseId(resultSet.getString("course_id"));
        return exam;
    }
}
