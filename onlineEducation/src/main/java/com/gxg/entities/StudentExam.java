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

    private String examName;

    private String courseName;

    private String teacherName;

    private String choiceQuestionScore;

    private String objectiveQuestionScore;

    private String rank;

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

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getObjectiveQuestionScore() {
        return objectiveQuestionScore;
    }

    public void setObjectiveQuestionScore(String objectiveQuestionScore) {
        this.objectiveQuestionScore = objectiveQuestionScore;
    }

    public String getChoiceQuestionScore() {
        return choiceQuestionScore;
    }

    public void setChoiceQuestionScore(String choiceQuestionScore) {
        this.choiceQuestionScore = choiceQuestionScore;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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
                ", examName='" + examName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", choiceQuestionScore='" + choiceQuestionScore + '\'' +
                ", objectiveQuestionScore='" + objectiveQuestionScore + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}
