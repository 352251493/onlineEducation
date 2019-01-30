package com.gxg.dao.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.rowmapper.CourseRowMapper;
import com.gxg.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2019/1/30 14:45
 */
@Repository(value = "courseDao")
public class CourseDaoImpl implements CourseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获得课程数量
     *
     * @return 课程数量
     * @author 郭欣光
     */
    @Override
    public int getCourseCount() {
        String sql = "select count(1) from course";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }


    /**
     * 获取指定范围按照修改时间排序的课程
     *
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLimitOrderByModifyTime(int limitStart, int limitEnd) {
        String sql = "select * from course order by modify_time desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), limitStart, limitEnd);
        return courseList;
    }
}
