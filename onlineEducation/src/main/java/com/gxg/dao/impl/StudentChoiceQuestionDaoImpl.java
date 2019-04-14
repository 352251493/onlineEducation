package com.gxg.dao.impl;

import com.gxg.dao.StudentChoiceQuestionDao;
import com.gxg.dao.rowmapper.StudentChoiceQuestionRowMapper;
import com.gxg.entities.StudentChoiceQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生选择题数据库相关
 * @author 郭欣光
 * @date 2019/4/14 17:52
 */
@Repository(value = "studentChoiceQuestionDao")
public class StudentChoiceQuestionDaoImpl implements StudentChoiceQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据学生考试ID及选择题ID获取学生选择题个数
     *
     * @param studentExamId    学生考试ID
     * @param choiceQuestionId 选择题ID
     * @return 学生选择题个数
     * @author 郭欣光
     */
    @Override
    public int getCountByStudentExamIdAndChoiceQuestionId(String studentExamId, String choiceQuestionId) {
        String sql = "select count(1) from student_choice_question where student_exam_id=? and choice_question_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, studentExamId, choiceQuestionId);
        return rowCount;
    }

    /**
     * 根据ID获取学生选择题信息数量
     *
     * @param id ID
     * @return 学生选择题信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from student_choice_question where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加学生选择题信息
     *
     * @param studentChoiceQuestion 学生选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int addStudentChoiceQuestion(StudentChoiceQuestion studentChoiceQuestion) {
        String sql = "insert into student_choice_question(id, student_exam_id, choice_question_id, answer, score) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, studentChoiceQuestion.getId(), studentChoiceQuestion.getStudentExamId(), studentChoiceQuestion.getChoiceQuestionId(), studentChoiceQuestion.getAnswer(), studentChoiceQuestion.getScore());
        return changeCount;
    }

    /**
     * 根据学生考试ID与选择题ID获取学生选择题信息
     *
     * @param studentExamId    学生考试ID
     * @param choiceQuestionId 选择题ID
     * @return 学生选择题信息
     * @author 郭欣光
     */
    @Override
    public List<StudentChoiceQuestion> getStudentChoiceQuestionByStudentExamIdAndChoiceQuestionId(String studentExamId, String choiceQuestionId) {
        String sql = "select * from student_choice_question where student_exam_id=? and choice_question_id=?";
        List<StudentChoiceQuestion> studentChoiceQuestionList = jdbcTemplate.query(sql, new StudentChoiceQuestionRowMapper(), studentExamId, choiceQuestionId);
        return studentChoiceQuestionList;
    }

    /**
     * 更新学生选择题信息
     *
     * @param studentChoiceQuestion 学生选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateStudentChoiceQuestion(StudentChoiceQuestion studentChoiceQuestion) {
        String sql = "update student_choice_question set student_exam_id=?, choice_question_id=?, answer=?, score=? where id=?";
        int changeCount = jdbcTemplate.update(sql, studentChoiceQuestion.getStudentExamId(), studentChoiceQuestion.getChoiceQuestionId(), studentChoiceQuestion.getAnswer(), studentChoiceQuestion.getScore(), studentChoiceQuestion.getId());
        return changeCount;
    }
}
