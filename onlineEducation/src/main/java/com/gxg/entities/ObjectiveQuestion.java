package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 客观题相关信息
 * @author 郭欣光
 * @date 2019/4/5 17:28
 */
public class ObjectiveQuestion {

    private String id;

    private String subject;

    private Timestamp createTime;

    private int score;

    private String examId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestion{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", createTime=" + createTime +
                ", score=" + score +
                ", examId='" + examId + '\'' +
                '}';
    }
}
