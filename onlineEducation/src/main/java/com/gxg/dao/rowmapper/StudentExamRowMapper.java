package com.gxg.dao.rowmapper;

import com.gxg.entities.StudentExam;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/4/13 15:03
 */
public class StudentExamRowMapper implements RowMapper<StudentExam> {

    @Override
    public StudentExam mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentExam studentExam = new StudentExam();
        studentExam.setId(resultSet.getString("id"));
        studentExam.setExamId(resultSet.getString("exam_id"));
        studentExam.setUserEmail(resultSet.getString("user_email"));
        studentExam.setScore(resultSet.getInt("score"));
        studentExam.setTime(resultSet.getString("time"));
        studentExam.setCreateTime(resultSet.getTimestamp("create_time"));
        return studentExam;
    }
}
