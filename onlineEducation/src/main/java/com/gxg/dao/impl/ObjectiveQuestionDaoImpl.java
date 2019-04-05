package com.gxg.dao.impl;

import com.gxg.dao.ObjectiveQuestionDao;
import com.gxg.entities.ObjectiveQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 问答题数据库相关
 * @author 郭欣光
 * @date 2019/4/5 18:27
 */

@Repository(value = "objectQuestionDao")
public class ObjectiveQuestionDaoImpl implements ObjectiveQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 根据ID获取问答题数量
     *
     * @param id ID
     * @return 问答题数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from objective_question where id=-?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加问答题
     *
     * @param objectiveQuestion 问答题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int addObjectiveQuestion(ObjectiveQuestion objectiveQuestion) {
        String sql = "insert into objective_question(id, subject, create_time, score, exam_id) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, objectiveQuestion.getId(), objectiveQuestion.getSubject(), objectiveQuestion.getCreateTime(), objectiveQuestion.getScore(), objectiveQuestion.getExamId());
        return changeCount;
    }
}
