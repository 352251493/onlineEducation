package com.gxg.dao;

import com.gxg.entities.Video;

import java.util.List;

/**
 * 课时视频数据库相关接口
 * @author 郭欣光
 * @date 2019/3/5 11:03
 */

public interface VideoDao {

    /**
     * 根据课时ID获取视频数量
     * @param lessonId 课时ID
     * @return 视频数量
     * @author 郭欣光
     */
    int getCountByLessonId(String lessonId);

    /**
     * 根据ID获取视频数量
     * @param id ID
     * @return 视频数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 创建视频
     * @param video 视频信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createVideo(Video video);

    /**
     * 根据课时ID获取视频
     * @param lessonId 课时ID
     * @return 视频相关信息
     * @author 郭欣光
     */
    List<Video> getVideoByLessonId(String lessonId);

    /**
     * 根据视频ID获取视频信息
     * @param id 视频ID
     * @return 视频信息
     * @author 郭欣光
     */
    Video getVideoById(String id);

    /**
     * 删除视频
     * @param video 视频信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteVideo(Video video);
}
