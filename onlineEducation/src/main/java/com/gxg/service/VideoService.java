package com.gxg.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 课时视频相关业务处理接口
 * @author 郭欣光
 * @date 2019/3/5 10:59
 */
public interface VideoService {

    /**
     * 根据课时ID获取视频个数
     * @param lessonId 课时ID
     * @return 视频个数先关信息
     * @author 郭欣光
     */
    String getVideoNumberByLessonId(String lessonId);

    /**
     * 创建课时视频
     * @param lessonId 课时ID
     * @param videoName 视频名称
     * @param videoFile 视频文件
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createVideo(String lessonId, String videoName, MultipartFile videoFile, HttpServletRequest request);
}
