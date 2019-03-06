package com.gxg.dao.impl;

import com.gxg.dao.VideoDao;
import com.gxg.dao.rowmapper.VideoRowMapper;
import com.gxg.entities.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 课时视频数据库先关处理
 * @author 郭欣光
 * @date 2019/3/5 11:04
 */

@Repository(value = "videoDao")
public class VideoDaoImpl implements VideoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据课时ID获取视频数量
     *
     * @param lessonId 课时ID
     * @return 视频数量
     * @author 郭欣光
     */
    @Override
    public int getCountByLessonId(String lessonId) {
        String sql = "select count(1) from video where lesson_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, lessonId);
        return rowCount;
    }

    /**
     * 根据ID获取视频数量
     *
     * @param id ID
     * @return 视频数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from video where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建视频
     *
     * @param video 视频信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createVideo(Video video) {
        String sql = "insert into video(id, name, path, create_time, lesson_id) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, video.getId(), video.getName(), video.getPath(), video.getCreateTime(), video.getLessonId());
        return changeCount;
    }

    /**
     * 根据课时ID获取视频
     *
     * @param lessonId 课时ID
     * @return 视频相关信息
     * @author 郭欣光
     */
    @Override
    public List<Video> getVideoByLessonId(String lessonId) {
        String sql = "select * from video where lesson_id=? order by create_time desc";
        List<Video> videoList = jdbcTemplate.query(sql, new VideoRowMapper(), lessonId);
        return videoList;
    }

    /**
     * 根据视频ID获取视频信息
     *
     * @param id 视频ID
     * @return 视频信息
     * @author 郭欣光
     */
    @Override
    public Video getVideoById(String id) {
        String sql = "select * from video where id=?";
        Video video = jdbcTemplate.queryForObject(sql, new VideoRowMapper(), id);
        return video;
    }

    /**
     * 删除视频
     *
     * @param video 视频信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteVideo(Video video) {
        String sql = "delete from video where id=?";
        int changeCount = jdbcTemplate.update(sql, video.getId());
        return changeCount;
    }
}
