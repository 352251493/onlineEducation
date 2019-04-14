package com.gxg.service.impl;

import com.gxg.dao.ChoiceQuestionDao;
import com.gxg.dao.StudentChoiceQuestionDao;
import com.gxg.dao.StudentExamDao;
import com.gxg.entities.ChoiceQuestion;
import com.gxg.entities.StudentChoiceQuestion;
import com.gxg.entities.StudentExam;
import com.gxg.entities.User;
import com.gxg.service.StudentChoiceQuestionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * @author 郭欣光
 * @date 2019/4/14 17:39
 */

@Service(value = "studentChoiceQuestionService")
public class StudentChoiceQuestionServiceImpl implements StudentChoiceQuestionService {

    @Autowired
    private ChoiceQuestionDao choiceQuestionDao;

    @Autowired
    private StudentExamDao studentExamDao;

    @Autowired
    private StudentChoiceQuestionDao studentChoiceQuestionDao;

    /**
     * 设置学生选择题答案
     * @param studentExamId 学生考试ID
     * @param choiceQuestionId 选择题ID
     * @param answer 答案
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String setStudentChoiceQuestionAnswer(String studentExamId, String choiceQuestionId, String answer, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "设置答案失败！";
        HttpSession session = request.getSession();
        answer = answer.toUpperCase();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户！";
        } else if ("A".equals(answer) || "B".equals(answer) || "C".equals(answer) || "D".equals(answer)) {
            if (studentExamDao.getCountById(studentExamId) == 0) {
                content = "没有找到该考试信息";
            } else {
                StudentExam studentExam = studentExamDao.getStudentExamById(studentExamId);
                User user = (User)session.getAttribute("user");
                if (user.getEmail().equals(studentExam.getUserEmail())) {
                    if (choiceQuestionDao.getCountById(choiceQuestionId) == 0) {
                        content = "没有该选择题";
                    } else if (studentChoiceQuestionDao.getCountByStudentExamIdAndChoiceQuestionId(studentExamId, choiceQuestionId) == 0) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String timeString = time.toString();
                        String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                        while (studentChoiceQuestionDao.getCountById(id) != 0) {
                            long idLong = Long.parseLong(id);
                            Random random = new Random();
                            idLong += random.nextInt(100);
                            id = idLong + "";
                            if (id.length() > 17) {
                                id = id.substring(0, 17);
                            }
                        }
                        StudentChoiceQuestion studentChoiceQuestion = new StudentChoiceQuestion();
                        studentChoiceQuestion.setId(id);
                        studentChoiceQuestion.setStudentExamId(studentExamId);
                        studentChoiceQuestion.setChoiceQuestionId(choiceQuestionId);
                        studentChoiceQuestion.setAnswer(answer);
                        studentChoiceQuestion.setScore(-1);
                        try {
                            if (studentChoiceQuestionDao.addStudentChoiceQuestion(studentChoiceQuestion) == 0) {
                                content = "操作数据库失败！";
                                System.out.println("ERROR:添加学生选择题信息" + studentChoiceQuestion.toString() + "操作数据库失败");
                            } else {
                                status = "true";
                                content = "设置成功！";
                            }
                        } catch (Exception e) {
                            content = "操作数据库失败！";
                            System.out.println("ERROR:添加学生选择题信息" + studentChoiceQuestion.toString() + "操作数据库失败，失败原因：" + e);
                        }
                    } else {
                        StudentChoiceQuestion studentChoiceQuestion = studentChoiceQuestionDao.getStudentChoiceQuestionByStudentExamIdAndChoiceQuestionId(studentExamId, choiceQuestionId).get(0);
                        if (answer.equals(studentChoiceQuestion.getAnswer())) {
                            status = "true";
                            content = "设置成功！";
                        } else {
                            studentChoiceQuestion.setAnswer(answer);
                            try {
                                if (studentChoiceQuestionDao.updateStudentChoiceQuestion(studentChoiceQuestion) == 0) {
                                    content = "操作数据库失败！";
                                    System.out.println("ERROR:修改用户选择题信息" + studentChoiceQuestion.toString() + "操作数据库失败");
                                } else {
                                    status = "true";
                                    content = "设置成功！";
                                }
                            } catch (Exception e) {
                                content = "操作数据库失败！";
                                System.out.println("ERROR:修改用户选择题信息" + studentChoiceQuestion.toString() + "操作数据库失败，失败原因：" + e);
                            }
                        }
                    }
                } else {
                    content = "登录用户与考试用户不一致！";
                }
            }
        } else {
            content = "答案必须为A/B/C/D";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 将答案赋值给选择题信息
     * @param choiceQuestionList 选择题信息
     * @param studentExam 学生考试信息
     * @return 选择题信息
     * @author 郭欣光
     */
    @Override
    public List<ChoiceQuestion> setAnswerForChoiceQuestion(List<ChoiceQuestion> choiceQuestionList, StudentExam studentExam) {
        if (choiceQuestionList != null) {
            String studentExamId = studentExam.getId();
            for (ChoiceQuestion choiceQuestion : choiceQuestionList) {
                if (studentChoiceQuestionDao.getCountByStudentExamIdAndChoiceQuestionId(studentExamId, choiceQuestion.getId()) != 0) {
                    StudentChoiceQuestion studentChoiceQuestion = studentChoiceQuestionDao.getStudentChoiceQuestionByStudentExamIdAndChoiceQuestionId(studentExamId, choiceQuestion.getId()).get(0);
                    choiceQuestion.setStudentAnswer(studentChoiceQuestion.getAnswer());
                }
            }
        }
        return choiceQuestionList;
    }
}
