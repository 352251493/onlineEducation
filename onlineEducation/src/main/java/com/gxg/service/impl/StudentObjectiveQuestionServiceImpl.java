package com.gxg.service.impl;

import com.gxg.dao.*;
import com.gxg.entities.*;
import com.gxg.service.StudentObjectiveQuestionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 学生客观题相关信息业务处理
 * @author 郭欣光
 * @date 2019/4/17 15:17
 */

@Service(value = "studentObjectiveQuestionService")
public class StudentObjectiveQuestionServiceImpl implements StudentObjectiveQuestionService {

    @Autowired
    private StudentExamDao studentExamDao;

    @Autowired
    private ObjectiveQuestionDao objectiveQuestionDao;

    @Autowired
    private StudentObjectiveQuestionDao studentObjectiveQuestionDao;

    @Autowired
    private ExamDao examDao;

    @Autowired
    private CourseDao courseDao;

    /**
     * 设置学生客观题信息答案
     *
     * @param studentExamId       学生考试ID
     * @param objectiveQuestionId 客观题信息ID
     * @param answer              答案
     * @param request             用户请求相关信息
     * @return 处理结果
     */
    @Override
    public String setStudentObjectiveQuestionAnswer(String studentExamId, String objectiveQuestionId, String answer, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "设置答案失败！";
        HttpSession session = request.getSession();
        answer = answer.toUpperCase();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户！";
        } else if (studentExamDao.getCountById(studentExamId) == 0) {
            content = "没有找到该考试信息";
        } else {
            StudentExam studentExam = studentExamDao.getStudentExamById(studentExamId);
            User user = (User)session.getAttribute("user");
            if (user.getEmail().equals(studentExam.getUserEmail())) {
                if (objectiveQuestionDao.getCountById(objectiveQuestionId) == 0) {
                    content = "没有该客观题题";
                } else if (studentObjectiveQuestionDao.getCountByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestionId) == 0) {
                    if (answer.length() > 5000) {
                        answer = answer.substring(5000);
                    }
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (studentObjectiveQuestionDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    StudentObjectiveQuestion studentObjectiveQuestion = new StudentObjectiveQuestion();
                    studentObjectiveQuestion.setId(id);
                    studentObjectiveQuestion.setStudentExamId(studentExamId);
                    studentObjectiveQuestion.setObjectiveQuestionId(objectiveQuestionId);
                    studentObjectiveQuestion.setAnswer(answer);
                    studentObjectiveQuestion.setScore(-1);
                    try {
                        if (studentObjectiveQuestionDao.addStudentObjectiveQuestion(studentObjectiveQuestion) == 0) {
                            content = "操作数据库失败！";
                            System.out.println("ERROR:添加学生客观题信息" + studentObjectiveQuestion.toString() + "操作数据库失败");
                        } else {
                            status = "true";
                            content = "设置成功！";
                        }
                    } catch (Exception e) {
                        content = "操作数据库失败！";
                        System.out.println("ERROR:添加学生客观题信息" + studentObjectiveQuestion.toString() + "操作数据库失败，失败原因：" + e);
                    }
                } else {
                    if (answer.length() > 5000) {
                        answer = answer.substring(5000);
                    }
                    StudentObjectiveQuestion studentObjectiveQuestion = studentObjectiveQuestionDao.getStudentObjectiveQuestionByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestionId).get(0);
                    if (answer.equals(studentObjectiveQuestion.getAnswer())) {
                        status = "true";
                        content = "设置成功！";
                    } else {
                        studentObjectiveQuestion.setAnswer(answer);
                        try {
                            if (studentObjectiveQuestionDao.updateStudentObjectiveQuestion(studentObjectiveQuestion) == 0) {
                                content = "操作数据库失败！";
                                System.out.println("ERROR:修改用户客观题题信息" + studentObjectiveQuestion.toString() + "操作数据库失败");
                            } else {
                                status = "true";
                                content = "设置成功！";
                            }
                        } catch (Exception e) {
                            content = "操作数据库失败！";
                            System.out.println("ERROR:修改用户客观题信息" + studentObjectiveQuestion.toString() + "操作数据库失败，失败原因：" + e);
                        }
                    }
                }
            } else {
                content = "登录用户与考试用户不一致！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 设置学生客观题答案
     *
     * @param objectiveQuestionList 客观题信息列表
     * @param studentExam           学生考试信息
     * @author 郭欣光
     */
    @Override
    public void setAnswerForObjectiveQuestion(List<ObjectiveQuestion> objectiveQuestionList, StudentExam studentExam) {
        if (objectiveQuestionList != null) {
            String studentExamId = studentExam.getId();
            for (ObjectiveQuestion objectiveQuestion : objectiveQuestionList) {
                if (studentObjectiveQuestionDao.getCountByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestion.getId()) != 0) {
                    StudentObjectiveQuestion studentObjectiveQuestion = studentObjectiveQuestionDao.getStudentObjectiveQuestionByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestion.getId()).get(0);
                    objectiveQuestion.setStudentAnswer(studentObjectiveQuestion.getAnswer());
                }
            }
        }
    }

    /**
     * 设置学生客观题成绩
     *
     * @param studentExamId       学生考试ID
     * @param objectiveQuestionId 客观题ID
     * @param score               成绩
     * @param request             用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String setStudentObjectiveStudentScore(String studentExamId, String objectiveQuestionId, String score, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "设置成绩失败！";
        HttpSession session = request.getSession();
        boolean isNumber = false;
        int scoreInt = -1;
        try {
            scoreInt = Integer.parseInt(score);
        } catch (Exception e) {
            content = "成绩必须为非负整数";
        }
        if (scoreInt >= 0) {
            if (session.getAttribute("user") == null) {
                content = "系统未检测到登录用户！";
            } else if (studentExamDao.getCountById(studentExamId) == 0) {
                content = "没有找到该考试信息";
            } else {
                StudentExam studentExam = studentExamDao.getStudentExamById(studentExamId);
                if (examDao.getCountById(studentExam.getExamId()) == 0) {
                    content = "系统未找到该考试！";
                } else {
                    Exam exam = examDao.getExamById(studentExam.getExamId());
                    if (courseDao.getCountById(exam.getCourseId()) == 0) {
                        content = "系统未找到该课程！";
                    } else {
                        Course course = courseDao.getCourseById(exam.getCourseId());
                        User user = (User)session.getAttribute("user");
                        if (user.getEmail().equals(course.getUserEmail())) {
                            if (objectiveQuestionDao.getCountById(objectiveQuestionId) == 0) {
                                content = "没有该客观题题";
                            } else if (studentObjectiveQuestionDao.getCountByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestionId) == 0) {
                                content = "学生没有做该客观题";
                            } else {
                                StudentObjectiveQuestion studentObjectiveQuestion = studentObjectiveQuestionDao.getStudentObjectiveQuestionByStudentExamIdAndObjectiveQuestionId(studentExamId, objectiveQuestionId).get(0);
                                if (scoreInt == studentObjectiveQuestion.getScore()) {
                                    status = "true";
                                    content = "设置成功！";
                                } else {
                                    studentObjectiveQuestion.setScore(scoreInt);
                                    try {
                                        if (studentObjectiveQuestionDao.updateScore(studentObjectiveQuestion) == 0) {
                                            content = "操作数据库失败！";
                                            System.out.println("ERROR:修改用户客观题题信息" + studentObjectiveQuestion.toString() + "成绩操作数据库失败");
                                        } else {
                                            status = "true";
                                            content = "设置成功！";
                                        }
                                    } catch (Exception e) {
                                        content = "操作数据库失败！";
                                        System.out.println("ERROR:修改用户客观题信息" + studentObjectiveQuestion.toString() + "成绩操作数据库失败，失败原因：" + e);
                                    }
                                }
                            }
                        } else {
                            content = "不可以为他人创建的考试设置成绩！";
                        }
                    }
                }
            }
        } else {
            content = "成绩必须为非负整数";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}
