package com.gxg.dao.rowmapper;

import com.gxg.entities.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/5/19 14:10
 */
public class CommentRowMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getString("id"));
        comment.setContent(resultSet.getString("content"));
        comment.setCreateTime(resultSet.getTimestamp("create_time"));
        comment.setUserEmail(resultSet.getString("user_email"));
        comment.setDiscussId(resultSet.getString("discuss_id"));
        return comment;
    }
}
