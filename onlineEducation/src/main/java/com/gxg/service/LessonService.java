package com.gxg.service;

import org.json.JSONObject;

/**
 * 课时相关业务处理接口
 * @author 郭欣光
 * @date 2019/2/19 10:24
 */

public interface LessonService {

    /**
     * 获取指定课程指定页数的课时信息
     * @param courseId 课程ID
     * @param lessonPage 页数
     * @return 课时相关信息
     * @author 郭欣光
     */
    JSONObject getLessonListByCourseId(String courseId, String lessonPage);
}
