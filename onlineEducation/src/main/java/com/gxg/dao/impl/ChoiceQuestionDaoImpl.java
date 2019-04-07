package com.gxg.dao.impl;

import com.gxg.dao.ChoiceQuestionDao;
import com.gxg.dao.rowmapper.ChoiceQuestionRowMapper;
import com.gxg.entities.ChoiceQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据考试ID获取选择题数量
     *
     * @param examId 考试ID
     * @return 选择题数量
     * @author 郭欣光
     */
    @Override
    public int getCountByExamId(String examId) {
        String sql = "select count(1) from choice_question where exam_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        return rowCount;
    }

    /**
     * 根据考试ID获取选择题信息
     *
     * @param examId 考试ID
     * @return 选择题信息
     * @author 郭欣光
     */
    @Override
    public List<ChoiceQuestion> getChoiceQuestionByExamId(String examId) {
        String sql = "select * from choice_question where exam_id=?";
        List<ChoiceQuestion> choiceQuestionList = jdbcTemplate.query(sql, new ChoiceQuestionRowMapper(), examId);
        return choiceQuestionList;
    }

    /**
     * 根据选择题ID获取选择题信息
     *
     * @param id 选择题ID
     * @return 选择题相关信息
     * @author 郭欣光
     */
    @Override
    public ChoiceQuestion getChoiceQuestionById(String id) {
        String sql = "select * from choice_question where id=?";
        ChoiceQuestion choiceQuestion = jdbcTemplate.queryForObject(sql, new ChoiceQuestionRowMapper(), id);
        return choiceQuestion;
    }

    /**
     * 删除选择题信息
     *
     * @param choiceQuestion 选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteChoiceQuestion(ChoiceQuestion choiceQuestion) {
        String sql = "delete from choice_question where id=?";
        int changeCount = jdbcTemplate.update(sql, choiceQuestion.getId());
        return changeCount;
    }
}
