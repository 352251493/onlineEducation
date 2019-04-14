package com.gxg.dao.rowmapper;

import com.gxg.entities.ChoiceQuestion;
import com.gxg.entities.StudentChoiceQuestion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/4/14 18:32
 */
public class StudentChoiceQuestionRowMapper implements RowMapper<StudentChoiceQuestion> {

    @Override
    public StudentChoiceQuestion mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentChoiceQuestion studentChoiceQuestion = new StudentChoiceQuestion();
        studentChoiceQuestion.setId(resultSet.getString("id"));
        studentChoiceQuestion.setStudentExamId(resultSet.getString("student_exam_id"));
        studentChoiceQuestion.setChoiceQuestionId(resultSet.getString("choice_question_id"));
        studentChoiceQuestion.setAnswer(resultSet.getString("answer"));
        studentChoiceQuestion.setScore(resultSet.getInt("score"));
        return studentChoiceQuestion;
    }
}
