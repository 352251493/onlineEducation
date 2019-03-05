package com.gxg.dao;

import com.gxg.entities.Video;

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
}
