package com.gxg.dao.impl;

import com.gxg.dao.MessageDao;
import com.gxg.dao.rowmapper.MessageRowMapper;
import com.gxg.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息通知数据库相关
 * @author 郭欣光
 * @date 2019/1/22 14:40
 */
@Repository(value = "/messageDao")
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据是否阅读查询消息通知数量
     *
     * @param isRead 是否阅读
     * @return 消息通知数量
     * @author 郭欣光
     */
    @Override
    public int getCountByIsRead(String isRead) {
        String sql = "select count(1) from message where is_read=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, isRead);
        return rowCount;
    }

    /**
     * 根据邮箱及是否阅读查询消息通知数量
     *
     * @param email  邮箱
     * @param isRead 是否阅读
     * @return 消息通知数量
     * @author 郭欣光
     */
    @Override
    public int getCountByEmailAndIsRead(String email, String isRead) {
        String sql = "select count(1) from message where email=? and is_read=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, email, isRead);
        return rowCount;
    }

    /**
     * 根据邮箱获取消息数量
     *
     * @param email 邮箱
     * @return 消息数量
     * @author 郭欣光
     */
    @Override
    public int getCountByEmail(String email) {
        String sql = "select count(1) from message where email=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return rowCount;
    }

    /**
     * 根据邮箱查询消息
     *
     * @param email 邮箱
     * @return 消息
     * @author 郭欣光
     */
    @Override
    public List<Message> getMessageByEmail(String email) {
        String sql = "select * from message where email=?";
        List<Message> messageList = jdbcTemplate.query(sql, new MessageRowMapper(), email);
        return messageList;
    }

    /**
     * 根据邮箱个limit获取消息
     *
     * @param email      邮箱
     * @param startLimit 第一个limit
     * @param endLimit   第二个limit
     * @return 消息列表
     * @author 郭欣光
     */
    @Override
    public List<Message> getMessageByEmailAndLimit(String email, int startLimit, int endLimit) {
        String sql = "select * from message where email=? limit ?, ?";
        List<Message> messageList = jdbcTemplate.query(sql, new MessageRowMapper(), email, startLimit, endLimit);
        return messageList;
    }

    /**
     * 根据邮箱 是否阅读以及limit获取消息
     *
     * @param email      邮箱
     * @param isRead     是否阅读
     * @param startLimit 第一个limit
     * @param endLimit   第二个limit
     * @return 消息列表
     * @author 郭欣光
     */
    @Override
    public List<Message> getMessageByEmailAndIsReadAndLimit(String email, String isRead, int startLimit, int endLimit) {
        String sql = "select * from message where email=? and is_read=? limit ?, ?";
        List<Message> messageList = jdbcTemplate.query(sql, new MessageRowMapper(), email, isRead, startLimit, endLimit);
        return messageList;
    }

    /**
     * 根据邮箱删除消息通知
     *
     * @param email 邮箱
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteMessageByEmail(String email) {
        String sql = "delete from message where email=?";
        int changeCount = jdbcTemplate.update(sql, email);
        return changeCount;
    }
}
