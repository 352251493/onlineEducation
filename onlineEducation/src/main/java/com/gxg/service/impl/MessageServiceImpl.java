package com.gxg.service.impl;

import com.gxg.dao.MessageDao;
import com.gxg.entities.Message;
import com.gxg.entities.User;
import com.gxg.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息通知相关业务处理
 * @author 郭欣光
 * @date 2019/1/22 11:23
 */
@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {

    private final String UNREAD_FLAGE = "0";

    private final String READ_FLAGE = "1";

    @Autowired
    private MessageDao messageDao;

    @Value("${message.count.each.page}")
    private int messageCountEachPage;

    /**
     * 获取用户未读信息数量
     *
     * @param user 用户信息
     * @return 未读消息数量
     * @author 郭欣光
     */
    @Override
    public int getUnreadMessageCount(User user) {
        int count = messageDao.getCountByEmailAndIsRead(user.getEmail(), UNREAD_FLAGE);
        return count;
    }

    /**
     * 获取用户消息通知列表
     *
     * @param user        用户信息
     * @param messageType 消息类型
     * @param messagePage 页码
     * @return 消息列表
     * @author 郭欣光
     */
    @Override
    public JSONObject getMessageList(User user, String messageType, String messagePage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int messagePageNumber = 1;
        int messagePageInt = 0;
        String hasMessage = "fasle";
        try {
            messagePageInt = Integer.parseInt(messagePage);
        } catch (Exception e) {
            messagePageInt = 0;
        }
        List<Message> messageList = null;
        if (messagePageInt > 0) {
            if ("all".equals(messageType)) {
                int messageCount = messageDao.getCountByEmail(user.getEmail());
                if (messageCount != 0) {
                    messagePageNumber = ((messageCount % messageCountEachPage) == 0) ? (messageCount / messageCountEachPage) : (messageCount / messageCountEachPage + 1);
                    if (messagePageInt <= messagePageNumber) {
                        status = "true";
                        hasMessage = "true";
                        messageList = messageDao.getMessageByEmailAndLimit(user.getEmail(), (messagePageInt - 1) * messageCountEachPage, messageCountEachPage);
                    }
                } else if (messagePageInt == 1) {
                    status = "true";
                }
            } else if ("unread".equals(messageType)) {
                int messageCount = messageDao.getCountByEmailAndIsRead(user.getEmail(), UNREAD_FLAGE);
                if (messageCount != 0) {
                    messagePageNumber = ((messageCount % messageCountEachPage) == 0) ? (messageCount / messageCountEachPage) : (messageCount / messageCountEachPage + 1);
                    if (messagePageInt <= messagePageNumber) {
                        status = "true";
                        hasMessage = "true";
                        messageList = messageDao.getMessageByEmailAndIsReadAndLimit(user.getEmail(), UNREAD_FLAGE, (messagePageInt - 1) * messageCountEachPage, messageCountEachPage);
                    }
                } else if (messagePageInt == 1) {
                    status = "true";
                }
            } else if ("read".equals(messageType)) {
                int messageCount = messageDao.getCountByEmailAndIsRead(user.getEmail(), READ_FLAGE);
                if (messageCount != 0) {
                    messagePageNumber = ((messageCount % messageCountEachPage) == 0) ? (messageCount / messageCountEachPage) : (messageCount / messageCountEachPage + 1);
                    if (messagePageInt <= messagePageNumber) {
                        status = "true";
                        hasMessage = "true";
                        messageList = messageDao.getMessageByEmailAndIsReadAndLimit(user.getEmail(), READ_FLAGE, (messagePageInt - 1) * messageCountEachPage, messageCountEachPage);
                    }
                } else if (messagePageInt == 1) {
                    status = "true";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("messagePageNumber", messagePageNumber);
        result.accumulate("messagePage", messagePageInt);
        result.accumulate("messageList", messageList);
        result.accumulate("hasMessage", hasMessage);
        return result;
    }
}
