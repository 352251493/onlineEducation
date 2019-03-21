package com.gxg.dao.impl;

import com.gxg.dao.ExamDao;
import com.gxg.entities.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 考试相关数据库信息
 * @author 郭欣光
 * @date 2019/3/21 11:22
 */
@Repository(value = "examDao")
public class ExamDaoImpl implements ExamDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过ID获取考试数量
     *
     * @param id ID
     * @return 考试数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from exam where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加考试
     *
     * @param exam 考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createExam(Exam exam) {
        String sql = "insert into exam(id, name, requirement, start_time, end_time, duration, create_time, modify_time, course_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, exam.getId(), exam.getName(), exam.getRequirement(), exam.getStartTime(), exam.getEndTime(), exam.getDuration(), exam.getCreateTime(), exam.getModifyTime(), exam.getCourseId());
        return changeCount;
    }
}
