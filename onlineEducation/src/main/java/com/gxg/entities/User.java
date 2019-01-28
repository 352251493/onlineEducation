package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 用户相关实体类
 * @author 郭欣光
 * @date 2019/1/16 15:50
 */
public class User {

    private String email;

    private String password;

    private String name;

    private String sex;

    private String role;

    private String headImage;

    private Timestamp createTime;

    private String isVerification;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getIsVerification() {
        return isVerification;
    }

    public void setIsVerification(String isVerification) {
        this.isVerification = isVerification;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", role='" + role + '\'' +
                ", headImage='" + headImage + '\'' +
                ", createTime=" + createTime +
                ", isVerification='" + isVerification + '\'' +
                '}';
    }
}
