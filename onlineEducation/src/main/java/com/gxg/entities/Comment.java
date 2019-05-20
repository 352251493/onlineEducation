package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 评论相关信息
 * @author 郭欣光
 * @date 2019/5/19 13:53
 */
public class Comment {

    private String id;

    private String content;

    private Timestamp createTime;

    private String userEmail;

    private String discussId;

    private String userName;

    private String userHeadImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDiscussId() {
        return discussId;
    }

    public void setDiscussId(String discussId) {
        this.discussId = discussId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", userEmail='" + userEmail + '\'' +
                ", discussId='" + discussId + '\'' +
                ", userName='" + userName + '\'' +
                ", userHeadImage='" + userHeadImage + '\'' +
                '}';
    }
}
