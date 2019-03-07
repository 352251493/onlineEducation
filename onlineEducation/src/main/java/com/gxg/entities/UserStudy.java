package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 用户学习相关信息
 * @author 郭欣光
 * @date 2019/3/7 10:44
 */
public class UserStudy {

    private String id;

    private String userEmail;

    private String courseId;

    private String isPrivate;

    private Timestamp createTime;

    private Timestamp lastStudyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastStudyTime() {
        return lastStudyTime;
    }

    public void setLastStudyTime(Timestamp lastStudyTime) {
        this.lastStudyTime = lastStudyTime;
    }

    @Override
    public String toString() {
        return "UserStudy{" +
                "id='" + id + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", courseId='" + courseId + '\'' +
                ", isPrivate='" + isPrivate + '\'' +
                ", createTime=" + createTime +
                ", lastStudyTime=" + lastStudyTime +
                '}';
    }
}
