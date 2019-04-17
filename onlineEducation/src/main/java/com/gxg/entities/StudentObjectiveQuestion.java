package com.gxg.entities;

/**
 * 学生客观题信息
 * @author 郭欣光
 * @date 2019/4/17 15:07
 */
public class StudentObjectiveQuestion {

    private String id;

    private String studentExamId;

    private String objectiveQuestionId;

    private String answer;

    private int score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentExamId() {
        return studentExamId;
    }

    public void setStudentExamId(String studentExamId) {
        this.studentExamId = studentExamId;
    }

    public String getObjectiveQuestionId() {
        return objectiveQuestionId;
    }

    public void setObjectiveQuestionId(String objectiveQuestionId) {
        this.objectiveQuestionId = objectiveQuestionId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "StudentObjectiveQuestion{" +
                "id='" + id + '\'' +
                ", studentExamId='" + studentExamId + '\'' +
                ", objectiveQuestionId='" + objectiveQuestionId + '\'' +
                ", answer='" + answer + '\'' +
                ", score=" + score +
                '}';
    }
}
