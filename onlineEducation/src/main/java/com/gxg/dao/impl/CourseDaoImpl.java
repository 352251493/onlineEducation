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

    /**
     * 获取指定范围按照学习人数排序的课程
     *
     * @param linitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLimitOrderByStudyNumber(int linitStart, int limitEnd) {
        String sql = "select * from course order by study_number desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), linitStart, limitEnd);
        return courseList;
    }

    /**
     * 获取与课程名称或教师名模糊查询的课程个数
     *
     * @param content 要查找的内容
     * @return 课程个数
     * @author 郭欣光
     */
    @Override
    public int getCountByLikeNameOrLikeUserName(String content) {
        content = "%" + content + "%";
        String sql = "select count(1) from (select distinct * from course where course.name like ? or course.user_email in (select user.email from user where user.name like ?)) a";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, content, content);
        return rowCount;
    }

    /**
     * 获取与课程名称或教师名模糊查询匹配及指定范围按照修改时间排序的课程列表
     *
     * @param content       要查找的内容
     * @param limitStart    第一个limit
     * @param limitStartEnd 第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLikeNameOrLikeUserNameAndLimitOrderByTime(String content, int limitStart, int limitStartEnd) {
        content = "%" + content + "%";
        String sql = "select distinct * from course where course.name like ? or course.user_email in (select user.email from user where user like ?) order by course.modify_time desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), content, content, limitStart, limitStartEnd);
        return courseList;
    }

    /**
     * 根据是否私有获取课程个数
     * @param isPrivate 是否私有
     * @return 课程个数
     * @author 郭欣光
     */
    @Override
    public int getCourseCountByIsPrivate(String isPrivate) {
        String sql = "select count(1) from course where is_private=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, isPrivate);
        return rowCount;
    }

    /**
     * 根据是否私有获取指定范围按照修改时间排序的课程
     *
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @param isPrivate  是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLimitAndIsPrivateOrderByModifyTime(int limitStart, int limitEnd, String isPrivate) {
        String sql = "select * from course where is_private=? order by modify_time desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), isPrivate, limitStart, limitEnd);
        return courseList;
    }

    /**
     * 根据是否私有获取指定范围按照学习人数排序的课程
     *
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @param isPrivate  是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLimitAndIsPrivateOrderByStudyNumber(int limitStart, int limitEnd, String isPrivate) {
        String sql = "select * from course where is_private=? order by study_number desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), isPrivate, limitStart, limitEnd);
        return courseList;
    }

    /**
     * 根据是否私有获取与课程名称或教师名模糊查询的课程个数
     *
     * @param content   要查找的内容
     * @param isPrivate 是否私有
     * @return 课程个数
     * @author 郭欣光
     */
    @Override
    public int getCountByLikeNameOrLikeUserNameAndIsPrivate(String content, String isPrivate) {
        content = "%" + content + "%";
        String sql = "select count(1) from (select distinct * from course where course.name like ? or course.user_email in (select user.email from user where user.name like ?)) a where a.is_private=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, content, content, isPrivate);
        return rowCount;
    }

    /**
     * 获取与指定是否私有、课程名称或教师名模糊查询匹配及指定范围按照修改时间排序的课程列表
     *
     * @param content       要查找的内容
     * @param limitStart    第一个limit
     * @param limitStartEnd 第二个limit
     * @param isPrivate     是否私有
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByLikeNameOrLikeUserNameAndLimitAndIsPrivateOrderByTime(String content, int limitStart, int limitStartEnd, String isPrivate) {
        content = "%" + content + "%";
        String sql = "select distinct * from course where course.is_private=? and (course.name like ? or course.user_email in (select user.email from user where user.name like ?)) order by course.modify_time desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), isPrivate, content, content, limitStart, limitStartEnd);
        return courseList;
    }

    /**
     * 根据ID获取课程数量
     *
     * @param id ID
     * @return 课程数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from course where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 添加课程信息
     *
     * @param course 课程信息
     * @return 操作数据库行数
     * @author 郭欣光
     */
    @Override
    public int createCourse(Course course) {
        String sql = "insert into course(id, name, introduction, image, study_number, create_time, modify_time, user_email, is_private) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, course.getId(), course.getName(), course.getIntroduction(), course.getImage(), course.getStudyNumber(), course.getCreateTime(), course.getModifyTime(), course.getUserEmail(), course.getIsPrivate());
        return changeCount;
    }

    /**
     * 获取指定用户的课程个数
     *
     * @param userEmail 用户邮箱
     * @return 课程个数
     * @author 郭欣光
     */
    @Override
    public int getCourseCountByUserEmail(String userEmail) {
        String sql = "select count(1) from course where user_email=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, userEmail);
        return rowCount;
    }

    /**
     * 获取指定用户指定范围的课程
     *
     * @param userEmail  用户邮箱
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseByUserEmailAndLimit(String userEmail, int limitStart, int limitEnd) {
        String sql = "select * from course where user_email=? order by modify_time desc limit ?, ?";
        List<Course> courseList = jdbcTemplate.query(sql, new CourseRowMapper(), userEmail, limitStart, limitEnd);
        return courseList;
    }

    /**
     * 根据ID获取课程信息
     *
     * @param id ID
     * @return 课程信息
     * @author 郭欣光
     */
    @Override
    public Course getCourseById(String id) {
        String sql = "select * from course where id=?";
        Course course = jdbcTemplate.queryForObject(sql, new CourseRowMapper(), id);
        return course;
    }

    /**
     * 修改课程信息
     *
     * @param course 课程信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int editCourse(Course course) {
        String sql = "update course set name=?, introduction=?, image=?, study_number=?, create_time=?, modify_time=?, user_email=?, is_private=? where id=?";
        int changeCount = jdbcTemplate.update(sql, course.getName(), course.getIntroduction(), course.getImage(), course.getStudyNumber(), course.getCreateTime(), course.getModifyTime(), course.getUserEmail(), course.getIsPrivate(), course.getId());
        return changeCount;
    }

    /**
     * 根据课程ID更新学习人数
     *
     * @param studyNumber 学习人数
     * @param id          课程ID
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateStudyNumberById(int studyNumber, String id) {
        String sql = "update course set study_number=? where id=?";
        int changeCount = jdbcTemplate.update(sql, studyNumber, id);
        return changeCount;
    }

    /**
     * 删除课程信息
     *
     * @param course 课程信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteCourse(Course course) {
        String sql = "delete from course where id=?";
        int changeCount = jdbcTemplate.update(sql, course.getId());
        return changeCount;
    }
}
