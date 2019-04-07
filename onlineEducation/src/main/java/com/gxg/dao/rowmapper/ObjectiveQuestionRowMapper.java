package com.gxg.dao.rowmapper;

import com.gxg.entities.ObjectiveQuestion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/4/7 15:44
 */
public class ObjectiveQuestionRowMapper implements RowMapper<ObjectiveQuestion> {

    @Override
    public ObjectiveQuestion mapRow(ResultSet resultSet, int i) throws SQLException {
        ObjectiveQuestion objectiveQuestion = new ObjectiveQuestion();
        objectiveQuestion.setId(resultSet.getString("id"));
        objectiveQuestion.setSubject(resultSet.getString("subject"));
        objectiveQuestion.setCreateTime(resultSet.getTimestamp("create_time"));
        objectiveQuestion.setScore(resultSet.getInt("score"));
        objectiveQuestion.setExamId(resultSet.getString("exam_id"));
        return objectiveQuestion;
    }
}
