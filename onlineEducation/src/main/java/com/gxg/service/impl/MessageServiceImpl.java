package com.gxg.service.impl;

import com.gxg.dao.MessageDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Message;
import com.gxg.entities.User;
import com.gxg.service.MailService;
import com.gxg.service.MessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    Configuration configuration;//这里注入的是freeMarker的configuration

    @Autowired
    private MailService mailService;

    @Value("${sys.name}")
    private String sysName;

    @Autowired
    private UserDao userDao;

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

    /**
     * 创建消息通知
     *
     * @param email          邮箱
     * @param title          标题
     * @param messageContent 内容
     * @return 结果
     * @author 郭欣光
     */
    @Override
    public synchronized JSONObject createMessage(String email, String title, String messageContent) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "创建消息通知失败！";
        Message message = new Message();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString();
        String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
        while (messageDao.getCountById(id) != 0) {
            long idLong = Long.parseLong(id);
            Random random = new Random();
            idLong += random.nextInt(100);
            id = idLong + "";
            if (id.length() > 17) {
                id = id.substring(0, 17);
            }
        }
        message.setId(id);
        message.setEmail(email);
        message.setTitle(title);
        message.setContent(messageContent);
        message.setCreateTime(time);
        message.setIsRead("0");
        message.setIsSend("0");
        try {
            if (messageDao.createMessage(message) == 0) {
                System.out.println("ERROR:创建消息{email：" + email + "，title:" + title + "，content:" + messageContent + "}操作数据库失败");
                content = "创建消息时操作数据库失败！";
            } else {
                status = "true";
                content = "创建成功！";
            }
        } catch (Exception e) {
            System.out.println("ERROR:创建消息{email：" + email + "，title:" + title + "，content:" + messageContent + "}操作数据库失败，失败原因：" + e);
            content = "创建消息时操作数据库失败！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }

    /**
     * 发送消息
     *
     * @param message 消息信息
     * @return 发送结果
     * @author 郭欣光
     */
    @Override
    public JSONObject sendMessage(Message message) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发送失败！";
        if (userDao.getUserCountByEmail(message.getEmail()) == 0) {
            content = "未找到消息所属用户！";
        } else {
            User user = userDao.getUserByEmail(message.getEmail());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("time", new Date());
            model.put("emailMessage", message.getContent());
            model.put("toUserName", user.getName());
            model.put("fromUserName", sysName);
            boolean isSendSuccess = false;
            try {
                Template t = configuration.getTemplate("email_html_temp.ftl");
                String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                JSONObject sendEmailResult = mailService.sendHtmlMail(message.getEmail(), message.getTitle(), emailContent);
                System.out.println("INFO:发送消息" + message.toString() + "结果：" + sendEmailResult.toString());
                if ("true".equals(sendEmailResult.getString("status"))) {
                    isSendSuccess = true;
                } else {
                    content = "发送消息" + message.toString() + "失败，失败原因：" + sendEmailResult.toString();
                }
            } catch (Exception e) {
                System.out.println("ERROR:发送消息" + message.toString() + "时发送邮件失败，失败原因：" + e);
                content = "发送消息" + message.toString() + "失败，失败原因：" + e;
            }
            if (isSendSuccess) {
                message.setIsSend("1");
                try {
                    if (messageDao.updateMessage(message) == 0) {
                        System.out.println("ERROR:发送消息" + message + "更新数据库失败");
                        content = "发送消息" + message + "更新数据库失败";
                    } else {
                        status = "true";
                        content = "消息" + message.toString() + "发送成功！";
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:发送消息" + message + "更新数据库失败，失败原因：" + e);
                    content = "发送消息" + message + "更新数据库失败，失败原因：" + e;
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }
}
