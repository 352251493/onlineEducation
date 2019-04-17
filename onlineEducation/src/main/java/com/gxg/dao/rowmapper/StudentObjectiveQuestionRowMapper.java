package com.gxg.dao.rowmapper;

import com.gxg.entities.StudentObjectiveQuestion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/4/17 16:11
 */
public class StudentObjectiveQuestionRowMapper implements RowMapper<StudentObjectiveQuestion> {

    @Override
    public StudentObjectiveQuestion mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentObjectiveQuestion studentObjectiveQuestion = new StudentObjectiveQuestion();
        studentObjectiveQuestion.setId(resultSet.getString("id"));
        studentObjectiveQuestion.setStudentExamId(resultSet.getString("student_exam_id"));
        studentObjectiveQuestion.setObjectiveQuestionId(resultSet.getString("objective_question_id"));
        studentObjectiveQuestion.setAnswer(resultSet.getString("answer"));
        studentObjectiveQuestion.setScore(resultSet.getInt("score"));
        return studentObjectiveQuestion;
    }
}
