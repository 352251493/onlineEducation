package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 课程先关实体类
 * @author 郭欣光
 * @date 2019/1/30 14:39
 */
public class Course {

    private String id;

    private String name;

    private String introduction;

    private String image;

    private int studyNumber;

    private Timestamp createTime;

    private Timestamp modifyTime;

    private String userEmail;

    private String userName;

    private String isPrivate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStudyNumber() {
        return studyNumber;
    }

    public void setStudyNumber(int studyNumber) {
        this.studyNumber = studyNumber;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", image='" + image + '\'' +
                ", studyNumber=" + studyNumber +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", isPrivate='" + isPrivate + '\'' +
                '}';
    }
}
