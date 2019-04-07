package com.gxg.dao.rowmapper;

import com.gxg.entities.ChoiceQuestion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/4/7 15:31
 */
public class ChoiceQuestionRowMapper implements RowMapper<ChoiceQuestion> {

    @Override
    public ChoiceQuestion mapRow(ResultSet resultSet, int i) throws SQLException {
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        choiceQuestion.setId(resultSet.getString("id"));
        choiceQuestion.setSubject(resultSet.getString("subject"));
        choiceQuestion.setOptionA(resultSet.getString("option_a"));
        choiceQuestion.setOptionB(resultSet.getString("option_b"));
        choiceQuestion.setOptionC(resultSet.getString("option_c"));
        choiceQuestion.setOptionD(resultSet.getString("option_d"));
        choiceQuestion.setAnswer(resultSet.getString("answer"));
        choiceQuestion.setCreateTime(resultSet.getTimestamp("create_time"));
        choiceQuestion.setExamId(resultSet.getString("exam_id"));
        choiceQuestion.setScore(resultSet.getInt("score"));
        return choiceQuestion;
    }
}
