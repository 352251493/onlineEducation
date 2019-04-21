package com.gxg.dao.impl;

import com.gxg.dao.StudentObjectiveQuestionDao;
import com.gxg.dao.rowmapper.StudentObjectiveQuestionRowMapper;
import com.gxg.entities.StudentObjectiveQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2019/4/17 15:31
 */

@Repository(value = "studentObjectiveQuestionDao")
public class StudentObjectiveQuestionDaoImpl implements StudentObjectiveQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据学生考试ID及客观题ID获取学生客观题信息个数
     *
     * @param studentExamId       学生考试ID
     * @param objectiveQuestionId 客观题ID
     * @return 学生客观题信息个数
     */
    @Override
    public int getCountByStudentExamIdAndObjectiveQuestionId(String studentExamId, String objectiveQuestionId) {
        String sql = "select count(1) from student_objective_question where student_exam_id=? and objective_question_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, studentExamId, objectiveQuestionId);
        return rowCount;
    }

    /**
     * 根据ID获取学生客观题信息个数
     *
     * @param id ID
     * @return 学生客观题信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from student_objective_question where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加学生客观题信息
     *
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int addStudentObjectiveQuestion(StudentObjectiveQuestion studentObjectiveQuestion) {
        String sql = "insert into student_objective_question(id, student_exam_id, objective_question_id, answer, score) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, studentObjectiveQuestion.getId(), studentObjectiveQuestion.getStudentExamId(), studentObjectiveQuestion.getObjectiveQuestionId(), studentObjectiveQuestion.getAnswer(), studentObjectiveQuestion.getScore());
        return changeCount;
    }

    /**
     * 根据学生考试ID和客观题ID获取学生客观题信息
     *
     * @param studentExamId       学生考试ID
     * @param objectiveQuestionId 客观题ID
     * @return 学生客观题信息
     * @author 郭欣光
     */
    @Override
    public List<StudentObjectiveQuestion> getStudentObjectiveQuestionByStudentExamIdAndObjectiveQuestionId(String studentExamId, String objectiveQuestionId) {
        String sql = "select * from student_objective_question where student_exam_id=? and objective_question_id=?";
        List<StudentObjectiveQuestion> objectiveQuestionList = jdbcTemplate.query(sql, new StudentObjectiveQuestionRowMapper(), studentExamId, objectiveQuestionId);
        return objectiveQuestionList;
    }

    /**
     * 更新学生客观题信息
     *
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateStudentObjectiveQuestion(StudentObjectiveQuestion studentObjectiveQuestion) {
        String sql = "update student_objective_question set student_exam_id=?, objective_question_id=?, answer=?, score=? where id=?";
        int changeCount = jdbcTemplate.update(sql, studentObjectiveQuestion.getStudentExamId(), studentObjectiveQuestion.getObjectiveQuestionId(), studentObjectiveQuestion.getAnswer(), studentObjectiveQuestion.getScore(), studentObjectiveQuestion.getId());
        return changeCount;
    }

    /**
     * 更新成绩
     *
     * @param studentObjectiveQuestion 学生客观题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateScore(StudentObjectiveQuestion studentObjectiveQuestion) {
        String sql = "update student_objective_question set score=? where id=?";
        int changeCount = jdbcTemplate.update(sql, studentObjectiveQuestion.getScore(), studentObjectiveQuestion.getId());
        return changeCount;
    }

    /**
     * 根据学生考试ID获取学生客观题信息数量
     *
     * @param studentExamId 学生考试ID
     * @return 学生客观题信息数量
     * @author 郭欣光
     */
    @Override
    public int getCountByStudentExamId(String studentExamId) {
        String sql = "select count(1) from student_objective_question where student_exam_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, studentExamId);
        return rowCount;
    }

    /**
     * 根据学生考试ID获取学生客观题信息
     *
     * @param studentExamId 学生考试ID
     * @return 学生客观题信息
     * @author 郭欣光
     */
    @Override
    public List<StudentObjectiveQuestion> getStudentObjectiveQuestionByStudentExamId(String studentExamId) {
        String sql = "select * from student_objective_question where student_exam_id=?";
        List<StudentObjectiveQuestion> studentObjectiveQuestionList = jdbcTemplate.query(sql, new StudentObjectiveQuestionRowMapper(), studentExamId);
        return studentObjectiveQuestionList;
    }
}
