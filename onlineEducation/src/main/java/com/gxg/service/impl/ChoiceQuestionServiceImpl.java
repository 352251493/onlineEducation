package com.gxg.service.impl;

import com.gxg.dao.ChoiceQuestionDao;
import com.gxg.dao.CourseDao;
import com.gxg.dao.ExamDao;
import com.gxg.entities.ChoiceQuestion;
import com.gxg.entities.Course;
import com.gxg.entities.Exam;
import com.gxg.entities.User;
import com.gxg.service.ChoiceQuestionService;
import com.gxg.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Random;

/**
 * @author 郭欣光
 * @date 2019/4/4 15:49
 */
@Service(value = "choiceQuestion")
public class ChoiceQuestionServiceImpl implements ChoiceQuestionService {

    @Autowired
    private ExamDao examDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ChoiceQuestionDao choiceQuestionDao;

    @Override
    public String addChoiceQuestion(String examId, String subject, String optionA, String optionB, String optionC, String optionD, String answer, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (examDao.getCountById(examId) == 0) {
            content = "系统获取考试信息失败！";
        } else if (StringUtils.isEmpty(subject)) {
            content = "题目不可以为空！";
        } else if (subject.length() > 1000) {
            content = "题目长度不能超过1000字符！";
        } else if (StringUtils.isEmpty(optionA)) {
            content = "选项A不能为空！";
        } else if (optionA.length() > 1000) {
            content = "选项A长度不能超过1000字符";
        } else if (StringUtils.isEmpty(optionB)) {
            content = "选项B不能为空！";
        } else if (optionB.length() > 1000) {
            content = "选项B长度不能超过1000字符";
        } else if (StringUtils.isEmpty(optionC)) {
            content = "选项C不能为空！";
        } else if (optionC.length() > 1000) {
            content = "选项C长度不能超过1000字符";
        } else if (StringUtils.isEmpty(optionD)) {
            content = "选项D不能为空！";
        } else if (optionD.length() > 1000) {
            content = "选项D长度不能超过1000字符";
        } else if (StringUtils.isEmpty(answer)) {
            content = "答案不能为空！";
        } else {
            answer = answer.toUpperCase();
            if ("A".equals(answer) || "B".equals(answer) || "c".equals(answer) || "D".equals(answer)) {
                Exam exam = examDao.getExamById(examId);
                Timestamp time = new Timestamp(System.currentTimeMillis());
                if (exam.getStartTime() != null && time.before(exam.getStartTime())) {
                    content = "考试已经开始，无法修改试题！";
                } else if (exam.getEndTime() != null && time.before(exam.getEndTime())) {
                    content = "开始已经结束，无法修改试题！";
                } else {
                    if (courseDao.getCountById(exam.getCourseId()) == 0) {
                        content = "系统获取课程信息失败！";
                    } else {
                        Course course = courseDao.getCourseById(exam.getCourseId());
                        User user = (User)session.getAttribute("user");
                        if (user.getEmail().equals(course.getUserEmail())) {
                            String timeString = time.toString();
                            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                            while (choiceQuestionDao.getCountById(id) != 0) {
                                long idLong = Long.parseLong(id);
                                Random random = new Random();
                                idLong += random.nextInt(100);
                                id = idLong + "";
                                if (id.length() > 17) {
                                    id = id.substring(0, 17);
                                }
                            }
                            ChoiceQuestion choiceQuestion = new ChoiceQuestion();
                            choiceQuestion.setId(id);
                            choiceQuestion.setSubject(subject);
                            choiceQuestion.setOptionA(optionA);
                            choiceQuestion.setOptionB(optionB);
                            choiceQuestion.setOptionC(optionC);
                            choiceQuestion.setOptionD(optionD);
                            choiceQuestion.setAnswer(answer);
                            choiceQuestion.setCreateTime(time);
                            choiceQuestion.setExamId(examId);
                            try {
                                if (choiceQuestionDao.addChoiceQuestion(choiceQuestion) == 0) {
                                    System.out.println("ERROR:为考试" + exam.toString() + "添加选择题" + choiceQuestion.toString() + "操作数据库失败");
                                    content = "操作数据库失败！";
                                } else {
                                    status = "true";
                                    content = "添加成功！";
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:为考试" + exam.toString() + "添加选择题" + choiceQuestion.toString() + "操作数据库失败，失败原因：" + e);
                                content = "操作数据库失败！";
                            }
                        } else {
                            content = "不可以为别人的课程添加试题！";
                        }
                        if ("status".equals(status)) {
                            exam.setModifyTime(time);
                            course.setModifyTime(time);
                            try {
                                if (examDao.updateExam(exam) == 0) {
                                    System.out.println("ERROR:添加选择题成功后修改考试" + exam.toString() + "操作数据库失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:添加选择题成功后修改考试" + exam.toString() + "操作数据库失败，失败原因：" + e);
                            }
                            try {
                                if (courseDao.editCourse(course) == 0) {
                                    System.out.println("ERROR:添加选择题成功后修改课程" + course.toString() + "操作数据库失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:添加选择题成功后修改课程" + course.toString() + "操作数据库失败，失败原因：" + e);
                            }
                        }
                    }
                }
            } else {
                content = "答案只能写A/B/C/D";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}
