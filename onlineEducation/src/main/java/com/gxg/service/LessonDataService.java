package com.gxg.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 课时资料相关业务处理接口
 * @author 郭欣光
 * @date 2019/2/28 10:52
 */
public interface LessonDataService {

    /**
     * 获取某一课时的资料数量
     * @param lessonId 课时ID
     * @return 处理结果
     * @author 郭欣光
     */
    String getLessonDataNumberByLessonId(String lessonId);

    /**
     * 创建课时资料
     * @param lessonId 课时ID
     * @param lessonDataFile 课时资料
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createLessonDate(String lessonId, MultipartFile lessonDataFile, HttpServletRequest request);
}
