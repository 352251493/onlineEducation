package com.gxg.entities;

/**
 * 学生选择题相关信息
 * @author 郭欣光
 * @date 2019/4/14 15:37
 */
public class StudentChoiceQuestion {

    private String id;

    private String studentExamId;

    private String choiceQuestionId;

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

    public String getChoiceQuestionId() {
        return choiceQuestionId;
    }

    public void setChoiceQuestionId(String choiceQuestionId) {
        this.choiceQuestionId = choiceQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "StudentChoiceQuestion{" +
                "id='" + id + '\'' +
                ", studentExamId='" + studentExamId + '\'' +
                ", choiceQuestionId='" + choiceQuestionId + '\'' +
                ", answer='" + answer + '\'' +
                ", score=" + score +
                '}';
    }
}
