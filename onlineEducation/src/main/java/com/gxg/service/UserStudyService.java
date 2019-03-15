package com.gxg.service;

import com.gxg.entities.Course;
import com.gxg.entities.User;
import com.gxg.entities.UserStudy;
import org.json.JSONObject;

import java.util.List;

/**
 * 用户学习信息相关业务处理接口
 * @author 郭欣光
 * @date 2019/3/7 10:47
 */
public interface UserStudyService {

    /**
     * 添加公有课程用户学习信息（如果已存在该用户学习该课程的信息，则更新时间即可）
     * @param user 用户信息
     * @param course 课程信息
     * @author 郭欣光
     */
    void addPublicUserStudy(User user, Course course);

    /**
     * 根据用户信息及课程类型获取指定页数用户学习课程信息
     * @param user 用户信息
     * @param isPrivate 课程类型
     * @param coursePage 指定页数
     * @return 课程信息
     * @author 郭欣光
     */
    JSONObject getStudyCourseByUserAndIsPrivate(User user, String isPrivate, String coursePage);

    /**
     * 根据课程ID获取用户学习信息
     * @param courseId 课程ID
     * @return 用户学习信息
     * @author 郭欣光
     */
    List<UserStudy> getUserStudyListByCourseId(String courseId);
}
