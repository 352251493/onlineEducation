package com.gxg.dao.impl;

import com.gxg.dao.LessonDao;
import com.gxg.dao.rowmapper.LessonRowMapper;
import com.gxg.entities.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2019/2/19 10:34
 */
@Repository(value = "lessonDao")
public class LessonDaoImpl implements LessonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据课程ID获取课时个数
     *
     * @param courseId 课程ID
     * @return 课时个数
     * @author 郭欣光
     */
    @Override
    public int getCountByCourseId(String courseId) {
        String sql = "select count(1) from lesson where course_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, courseId);
        return rowCount;
    }

    /**
     * 根据课程ID按照修改时间获取指定范围的课时信息
     *
     * @param courseId   课程ID
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 课时信息
     * @author 郭欣光
     */
    @Override
    public List<Lesson> getLessonByCourseIdAndLimitOrderByModifyTime(String courseId, int limitStart, int limitEnd) {
        String sql = "select * from lesson where course_id=? order by modify_time desc limit ?, ?";
        List<Lesson> lessonList = jdbcTemplate.query(sql, new LessonRowMapper(), courseId, limitStart, limitEnd);
        return lessonList;
    }
}
