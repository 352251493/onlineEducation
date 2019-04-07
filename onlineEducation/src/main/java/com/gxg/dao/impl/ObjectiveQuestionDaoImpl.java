package com.gxg.dao.impl;

import com.gxg.dao.ObjectiveQuestionDao;
import com.gxg.dao.rowmapper.ObjectiveQuestionRowMapper;
import com.gxg.entities.ObjectiveQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        String sql = "select count(1) from objective_question where id=?";
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

    /**
     * 根据考试ID获取客观题数量
     *
     * @param examId 考试ID
     * @return 客观题数量
     * @author 郭欣光
     */
    @Override
    public int getCountByExamId(String examId) {
        String sql = "select count(1) from objective_question where exam_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        return rowCount;
    }

    /**
     * 根据考试ID获取客观题信息
     *
     * @param examId 考试ID
     * @return 客观题信息
     * @author 郭欣光
     */
    @Override
    public List<ObjectiveQuestion> getObjectiveQuestionByExamId(String examId) {
        String sql = "select * from objective_question where exam_id=?";
        List<ObjectiveQuestion> objectiveQuestionList = jdbcTemplate.query(sql, new ObjectiveQuestionRowMapper(), examId);
        return objectiveQuestionList;
    }


    /**
     * 根据客观题ID获取客观题信息
     *
     * @param id 客观题ID
     * @return 客观题信息
     * @author 郭欣光
     */
    @Override
    public ObjectiveQuestion getObjectiveQuestionById(String id) {
        String sql = "select * from objective_question where id=?";
        ObjectiveQuestion objectiveQuestion = jdbcTemplate.queryForObject(sql, new ObjectiveQuestionRowMapper(), id);
        return objectiveQuestion;
    }

    /**
     * 删除客观题
     *
     * @param objectiveQuestion 客观题相关信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteObjectiveQuestion(ObjectiveQuestion objectiveQuestion) {
        String sql = "delete from objective_question where id=?";
        int changeCount = jdbcTemplate.update(sql, objectiveQuestion.getId());
        return changeCount;
    }
}
