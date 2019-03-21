package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 考试相关信息
 * @author 郭欣光
 * @date 2019/3/21 10:41
 */
public class Exam {

    private String id;

    private String name;

    private String requirement;

    private Timestamp startTime;

    private Timestamp endTime;

    private int duration;

    private Timestamp createTime;

    private Timestamp modifyTime;

    private String courseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", requirement='" + requirement + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
