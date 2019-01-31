package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Course;
import com.gxg.entities.User;
import com.gxg.service.CourseService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程相关业务处理
 * @author 郭欣光
 * @date 2019/1/30 14:35
 */

@Service(value = "courseService")
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Value("${course.count.each.page}")
    private int courseCountEachPage;

    @Autowired
    private UserDao userDao;

    /**
     * 根据指定页数按照修改时间顺序获取课程列表
     *
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCourseListOrderByModifyTime(String coursePage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int coursePageNumber = 1;
        int coursePageInt = 0;
        String hasCourse = "false";
        try {
            coursePageInt = Integer.parseInt(coursePage);
        } catch (Exception e) {
            coursePageInt = 0;
        }
        List<Course> courseList = null;
        if (coursePageInt > 0) {
            int courseCount = courseDao.getCourseCount();
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLimitOrderByModifyTime(coursePageInt - 1, courseCountEachPage);
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
                    }
                }
            } else if (coursePageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("coursePageNumber", coursePageNumber);
        result.accumulate("coursePage", coursePageInt);
        result.accumulate("courseList", courseList);
        result.accumulate("hasCourse", hasCourse);
        return result;
    }

    /**
     * 根据指定页数按照学习人数顺序获取课程列表
     *
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCourseListOrderByStudyNumber(String coursePage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int coursePageNumber = 1;
        int coursePageInt = 0;
        String hasCourse = "false";
        try {
            coursePageInt = Integer.parseInt(coursePage);
        } catch (Exception e) {
            coursePageInt = 0;
        }
        List<Course> courseList = null;
        if (coursePageInt > 0) {
            int courseCount = courseDao.getCourseCount();
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLimitOrderByStudyNumber(coursePageInt - 1, courseCountEachPage);
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
                    }
                }
            } else if (coursePageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("coursePageNumber", coursePageNumber);
        result.accumulate("coursePage", coursePageInt);
        result.accumulate("courseList", courseList);
        result.accumulate("hasCourse", hasCourse);
        return result;
    }


    /**
     * 根据搜索内容和指定页数获取课程列表
     * @param searchContent 搜索内容
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject searchCourse(String searchContent, String coursePage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int coursePageNumber = 1;
        int coursePageInt = 0;
        String hasCourse = "false";
        try {
            coursePageInt = Integer.parseInt(coursePage);
        } catch (Exception e) {
            coursePageInt = 0;
        }
        List<Course> courseList = null;
        if (coursePageInt > 0) {
            int courseCount = courseDao.getCountByLikeNameOrLikeUserName(searchContent);
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLikeNameOrLikeUserNameAndLimitOrderByTime(searchContent, coursePageInt - 1, courseCountEachPage);
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
                    }
                }
            } else if (coursePageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("coursePageNumber", coursePageNumber);
        result.accumulate("coursePage", coursePageInt);
        result.accumulate("courseList", courseList);
        result.accumulate("hasCourse", hasCourse);
        return result;
    }
}
