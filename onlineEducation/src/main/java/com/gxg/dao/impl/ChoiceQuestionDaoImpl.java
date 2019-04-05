package com.gxg.dao.impl;

import com.gxg.dao.ChoiceQuestionDao;
import com.gxg.entities.ChoiceQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 选择题数据库相关
 * @author 郭欣光
 * @date 2019/4/4 16:07
 */

@Repository(value = "choiceQuestionDao")
public class ChoiceQuestionDaoImpl implements ChoiceQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据ID获取选择题个数
     *
     * @param id ID
     * @return 选择题个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from choice_question where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加选择题
     *
     * @param choiceQuestion 选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int addChoiceQuestion(ChoiceQuestion choiceQuestion) {
        String sql = "insert into choice_question(id, subject, option_a, option_b, option_c, option_d, answer, create_time, exam_id, score) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, choiceQuestion.getId(), choiceQuestion.getSubject(), choiceQuestion.getOptionA(), choiceQuestion.getOptionB(), choiceQuestion.getOptionC(), choiceQuestion.getOptionD(), choiceQuestion.getAnswer(), choiceQuestion.getCreateTime(), choiceQuestion.getExamId(), choiceQuestion.getScore());
        return changeCount;
    }
}
