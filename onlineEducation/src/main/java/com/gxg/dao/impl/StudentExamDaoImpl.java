package com.gxg.dao.impl;

import com.gxg.dao.StudentExamDao;
import com.gxg.dao.rowmapper.StudentExamRowMapper;
import com.gxg.entities.StudentExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学生考试数据库相关
 * @author 郭欣光
 * @date 2019/4/13 14:57
 */

@Repository(value = "studentExamDao")
public class StudentExamDaoImpl implements StudentExamDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据考试ID和用户邮箱获取用户考试信息个数
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 考试信息个数
     */
    @Override
    public int getCountByExamIdAndUserEmail(String examId, String userEmail) {
        String sql = "select count(1) from student_exam where exam_id=? and user_email=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, examId, userEmail);
        return rowCount;
    }

    /**
     * 根据考试ID及用户邮箱获取学生考试信息
     *
     * @param examId    考试ID
     * @param userEmail 用户邮箱
     * @return 学生考试信息
     * @author 郭欣光
     */
    @Override
    public List<StudentExam> getStudentExamByExamIdAndUserEmail(String examId, String userEmail) {
        String sql = "select * from student_exam where exam_id=? and user_email=?";
        List<StudentExam> studentExamList = jdbcTemplate.query(sql, new StudentExamRowMapper(), examId, userEmail);
        return studentExamList;
    }

    /**
     * 根据ID获取学生考试信息个数
     *
     * @param id ID
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from student_exam where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加学生考试信息
     *
     * @param studentExam 学生考试信息
     * @return 数据库改变行数
     */
    @Override
    public int addStudentExam(StudentExam studentExam) {
        String sql = "insert into student_exam(id, exam_id, user_email, score, time, create_time) values(?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, studentExam.getId(), studentExam.getExamId(), studentExam.getUserEmail(), studentExam.getScore(), studentExam.getTime(), studentExam.getCreateTime());
        return changeCount;
    }

    /**
     * 根据考试ID及用户邮箱更新考试剩余时间
     *
     * @param time      考试剩余时间
     * @param examId    考试ID
     * @param userEmail 用户邮箱
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateStudentExamTimeByExamIdAndUserEmail(String time, String examId, String userEmail) {
        String sql = "update student_exam set time=? where exam_id=? and user_email=?";
        int changeCount = jdbcTemplate.update(sql, time, examId, userEmail);
        return changeCount;
    }

    /**
     * 根据ID获取学生考试信息
     *
     * @param id ID
     * @return 学生考试信息
     * @author 郭欣光
     */
    @Override
    public StudentExam getStudentExamById(String id) {
        String sql = "select * from student_exam where id=?";
        StudentExam studentExam = jdbcTemplate.queryForObject(sql, new StudentExamRowMapper(), id);
        return studentExam;
    }

    /**
     * 根据考试ID获取学生考试信息个数
     *
     * @param examId 考试ID
     * @return 学生考试信息个数
     * @author 郭欣光
     */
    @Override
    public int getCountByExamId(String examId) {
        String sql = "select count(1) from student_exam where exam_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, examId);
        return rowCount;
    }

    /**
     * 根据考试ID按照创建时间排序获取指定位置的学生考试信息
     *
     * @param examId     考试ID
     * @param startLimit 第一个Limit
     * @param endLimit   第二个Limit
     * @return 学生考试信息
     * @author 郭欣光
     */
    @Override
    public List<StudentExam> getStudentExamByExamIdAndLimitOrderByCreateTime(String examId, int startLimit, int endLimit) {
        String sql = "select * from student_exam where exam_id=? order by create_time limit ?, ?";
        List<StudentExam> studentExamList = jdbcTemplate.query(sql, new StudentExamRowMapper(), examId, startLimit, endLimit);
        return studentExamList;
    }
}
