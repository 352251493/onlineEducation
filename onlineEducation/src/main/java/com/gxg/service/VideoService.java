package com.gxg.service;

import com.gxg.entities.Video;
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

    /**
     * 根据课时ID获取视频列表
     * @param lessonId 课时ID
     * @return 视频列表相关信息
     * @author 郭欣光
     */
    String getVideoListByLessonId(String lessonId);

    /**
     * 根据ID获取视频信息
     * @param id 视频ID
     * @return 视频信息
     * @author 郭欣光
     */
    Video getVideoById(String id);

    /**
     * 删除视频
     * @param videoId 视频ID
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteVideo(String videoId, HttpServletRequest request);
}
