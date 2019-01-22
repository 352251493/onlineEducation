package com.gxg.dao.rowmapper;

import com.gxg.entities.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/1/22 15:19
 */
public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getString("id"));
        message.setEmail(resultSet.getString("email"));
        message.setTitle(resultSet.getString("title"));
        message.setContent(resultSet.getString("content"));
        message.setCreateTime(resultSet.getTimestamp("create_time"));
        message.setIsSend(resultSet.getString("is_send"));
        message.setIsRead(resultSet.getString("is_read"));
        return message;
    }
}
