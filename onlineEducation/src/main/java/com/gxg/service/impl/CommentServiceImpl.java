package com.gxg.service.impl;

import com.gxg.dao.CommentDao;
import com.gxg.dao.DiscussDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.*;
import com.gxg.service.CommentService;
import com.gxg.service.MessageService;
import com.gxg.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 评论相关业务处理
 * @author 郭欣光
 * @date 2019/5/19 13:48
 */
@Service(value = "commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Value("${comment.count.each.page}")
    private int commentCountEachPage;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DiscussDao discussDao;

    @Autowired
    private MessageService messageService;

    /**
     * 获取指定讨论帖子指定页数的评论信息
     *
     * @param discussId   讨论帖子ID
     * @param commentPage 评论页数
     * @return 评论相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCommentListByDiscussId(String discussId, String commentPage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int commentPageNumber = 1;
        int commentPageInt = 0;
        String hasComment = "false";
        try {
            commentPageInt = Integer.parseInt(commentPage);
        } catch (Exception e) {
            commentPageInt = 0;
        }
        List<Comment> commentList = null;
        if (commentPageInt > 0) {
            int commentCount = commentDao.getCountByDiscussId(discussId);
            if (commentCount != 0) {
                commentPageNumber = ((commentCount % commentCountEachPage) == 0) ? (commentCount / commentCountEachPage) : (commentCount / commentCountEachPage + 1);
                if (commentPageInt <= commentPageNumber) {
                    status = "true";
                    hasComment = "true";
                    commentList = commentDao.getCommentByDiscussIdAndLimitOrderByCreateTime(discussId, commentPageInt - 1, commentCountEachPage);
                    if (commentList != null && commentList.size() > 0) {
                        for (Comment comment : commentList) {
                            if (userDao.getUserCountByEmail(comment.getUserEmail()) != 0) {
                                User user = userDao.getUserByEmail(comment.getUserEmail());
                                comment.setUserName(user.getName());
                                comment.setUserHeadImage(user.getHeadImage());
                            }
                        }
                    }
                }
            } else if (commentPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("commentPageNumber", commentPageNumber);
        result.accumulate("commentPage", commentPageInt);
        result.accumulate("commentList", commentList);
        result.accumulate("hasComment", hasComment);
        return result;
    }

    /**
     * 创建评论
     *
     * @param discussId      讨论帖子ID
     * @param commentContent 评论内容
     * @param request        用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createComment(String discussId, String commentContent, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发表失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (discussDao.getCountById(discussId) == 0) {
            content = "系统未检测到讨论帖子信息";
        } else if (StringUtils.isEmpty(commentContent)) {
            content = "写点什么呗~";
        } else if (commentContent.length() > 1000) {
            content = "评论内容不能超过1000字符！";
        } else {
            User user = (User)session.getAttribute("user");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String timeString = time.toString();
            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
            while (commentDao.getCountById(id) != 0) {
                long idLong = Long.parseLong(id);
                Random random = new Random();
                idLong += random.nextInt(100);
                id = idLong + "";
                if (id.length() > 17) {
                    id = id.substring(0, 17);
                }
            }
            Comment comment = new Comment();
            comment.setId(id);
            comment.setContent(commentContent);
            comment.setCreateTime(time);
            comment.setUserEmail(user.getEmail());
            comment.setDiscussId(discussId);
            try {
                if (commentDao.createComment(comment) == 0) {
                    System.out.println("ERROR:创建评论" + comment.toString() + "操作数据库失败！");
                    content = "操作数据库失败！";
                } else {
                    status = "true";
                    content = "发表成功！";
                }
            } catch (Exception e) {
                System.out.println("ERROR:创建评论" + comment.toString() + "操作数据库失败，失败原因：" + e);
                content = "操作数据库失败！";
            }
            if ("true".equals(status)) {
                if (discussDao.getCountById(discussId) != 0) {
                    Discuss discuss = discussDao.getDiscussById(discussId);
                    if (userDao.getUserCountByEmail(discuss.getUserEmail()) != 0) {
                        User discussUser = userDao.getUserByEmail(discuss.getUserEmail());
                        String messageTitle = "您有一条新评论";
                        String messageContent = createCreateCommentSuccessEmailMessage(comment, user, discuss);
                        JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                        System.out.println("INFO:评论" + comment.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateCommentSuccessEmailMessage(Comment comment, User user, Discuss discuss) {
        String timeString = comment.getCreateTime().toString().split("\\.")[0];
        String message = "<p>" + user.getName() + "于" + timeString + "在讨论区评论了您的讨论帖子:" + discuss.getName() + "内容如下：</p>";
        message += "<p style='color: red;'>" + comment.getContent() + "</p>";
        return message;
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @param request   用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteComment(String commentId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (commentDao.getCountById(commentId) == 0) {
            content = "系统未检测到评论信息！";
        } else {
            Comment comment = commentDao.getCommentById(commentId);
            User user = (User)session.getAttribute("user");
            if (user.getEmail().equals(comment.getUserEmail())) {
                try {
                    if (commentDao.deleteComment(comment) == 0) {
                        System.out.println("ERROR:删除评论" + comment.toString() + "操作数据库失败！");
                        content = "操作数据库失败！";
                    } else {
                        status = "true";
                        content = "删除成功！";
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:删除评论" + comment.toString() + "操作数据库失败，失败原因：" + e);
                    content = "操作数据库失败！";
                }
            } else {
                content = "只能删除自己发表的评论！";
            }
            if ("true".equals(status)) {
                String messageTitle = "删除评论成功";
                String messageContent = createDeleteCommentSuccessEmailMessage(comment);
                JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                System.out.println("INFO:评论" + content.toString() + "删除成功时创建消息通知结果：" + createMessageResult.toString());
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteCommentSuccessEmailMessage(Comment comment) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "成功删除评论：" + comment.getContent() + "</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}
