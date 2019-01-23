package com.gxg.dao;

import com.gxg.entities.Message;

import java.util.List;

/**
 * 消息通知数据库相关接口
 * @author 郭欣光
 * @date 2019/1/22 14:37
 */
public interface MessageDao {

    /**
     * 根据是否阅读查询消息通知数量
     * @param isRead 是否阅读
     * @return 消息通知数量
     * @author 郭欣光
     */
    int getCountByIsRead(String isRead);

    /**
     * 根据邮箱及是否阅读查询消息通知数量
     * @param email 邮箱
     * @param isRead 是否阅读
     * @return 消息通知数量
     * @author 郭欣光
     */
    int getCountByEmailAndIsRead(String email, String isRead);

    /**
     * 根据邮箱获取消息数量
     * @param email 邮箱
     * @return 消息数量
     * @author 郭欣光
     */
    int getCountByEmail(String email);

    /**
     * 根据邮箱查询消息
     * @param email 邮箱
     * @return 消息
     * @author 郭欣光
     */
    List<Message> getMessageByEmail(String email);

    /**
     * 根据邮箱个limit获取消息
     * @param email 邮箱
     * @param startLimit 第一个limit
     * @param endLimit 第二个limit
     * @return 消息列表
     * @author 郭欣光
     */
    List<Message> getMessageByEmailAndLimit(String email, int startLimit, int endLimit);

    /**
     * 根据邮箱 是否阅读以及limit获取消息
     * @param email 邮箱
     * @param isRead 是否阅读
     * @param startLimit 第一个limit
     * @param endLimit 第二个limit
     * @return 消息列表
     * @author 郭欣光
     */
    List<Message> getMessageByEmailAndIsReadAndLimit(String email,String isRead, int startLimit, int endLimit);

    /**
     * 根据邮箱删除消息通知
     * @param email 邮箱
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteMessageByEmail(String email);
}
