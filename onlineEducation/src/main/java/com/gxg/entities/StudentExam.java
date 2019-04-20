package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 学生考试信息
 * @author 郭欣光
 * @date 2019/4/13 14:42
 */
public class StudentExam {

    private String id;

    private String examId;

    private String userEmail;

    private int Score;

    private String time;

    private Timestamp createTime;

    private String studentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "StudentExam{" +
                "id='" + id + '\'' +
                ", examId='" + examId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", Score=" + Score +
                ", time='" + time + '\'' +
                ", createTime=" + createTime +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
