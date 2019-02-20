package com.gxg.service;

import com.gxg.entities.Course;
import com.gxg.entities.User;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 课程相关业务处理
 * @author 郭欣光
 * @date 2019/1/30 14:33
 */

public interface CourseService {

    /**
     * 根据指定页数按照修改时间顺序获取课程列表
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject getCourseListOrderByModifyTime(String coursePage);

    /**
     * 根据指定页数按照学习人数顺序获取课程列表
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject getCourseListOrderByStudyNumber(String coursePage);

    /**
     * 根据搜索内容和指定页数获取课程列表
     * @param searchContent 搜索内容
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    JSONObject searchCourse(String searchContent, String coursePage);

    /**
     * 创建课程业务逻辑处理
     * @param courseName 课程名称
     * @param courseIntroduction 课程简介
     * @param courseImage 课程封面图片
     * @param isPrivate 是否私有
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createCourse(String courseName, String courseIntroduction, MultipartFile courseImage, String isPrivate, HttpServletRequest request);

    /**
     * 获取指定用户的前N个课程
     * @param user 用户信息
     * @param topNumber 个数
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getUserCourseByTopNumber(User user, int topNumber);

    /**
     * 根据ID获取课程信息
     * @param courseId 课程ID
     * @return 课程信息
     * @author 郭欣光
     */
    Course getCourseById(String courseId);

    /**
     * 修改课程资料
     * @param courseId 课程ID
     * @param courseName 课程名称
     * @param courseIntroduction 课程简介
     * @param request 用户请求信息
     * @return 修改结果
     * @author 郭欣光
     */
    String editCourse(String courseId, String courseName, String courseIntroduction, HttpServletRequest request);

    /**
     * 修改课程封面图片
     * @param courseId 课程ID
     * @param courseImage 封面图片
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String editCourseImage(String courseId, MultipartFile courseImage, HttpServletRequest request);

    /**
     * 获取指定课程私有类型按照修改时间排序后的前N个课程
     * @param isPrivate 课程私有类型
     * @param topNumber N
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseListByIsPrivateAndTopNumberOrderByModifyTime(String isPrivate, int topNumber);

    /**
     * 获取指定课程私有类型按照学习人数排序后的前N个课程
     * @param isPrivate 课程私有类型
     * @param topNumber N
     * @return 课程列表
     * @author 郭欣光
     */
    List<Course> getCourseListByIsPrivateAndTopNumberOrderByStudyNumber(String isPrivate, int topNumber);

    /**
     * 获取用户课程信息
     * @param user 用户信息
     * @param coursePage 课程页数
     * @return 课程先关信息
     * @author 郭欣光
     */
    JSONObject getCourseByUser(User user, String coursePage);
}
