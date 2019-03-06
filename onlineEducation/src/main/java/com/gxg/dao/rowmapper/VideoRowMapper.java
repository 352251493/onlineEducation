package com.gxg.dao.rowmapper;

import com.gxg.entities.Video;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/3/6 10:07
 */
public class VideoRowMapper implements RowMapper<Video> {

    @Override
    public Video mapRow(ResultSet resultSet, int i) throws SQLException {
        Video video = new Video();
        video.setId(resultSet.getString("id"));
        video.setName(resultSet.getString("name"));
        video.setPath(resultSet.getString("path"));
        video.setCreateTime(resultSet.getTimestamp("create_time"));
        video.setLessonId(resultSet.getString("lesson_id"));
        return video;
    }
}
