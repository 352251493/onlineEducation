package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.ExamDao;
import com.gxg.dao.ObjectiveQuestionDao;
import com.gxg.entities.*;
import com.gxg.service.ObjectiveQuestionService;
import com.gxg.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 问答题相关业务处理
 * @author 郭欣光
 * @date 2019/4/5 18:20
 */

@Service(value = "objectiveQuestion")
public class ObjectiveQuestionServiceImpl implements ObjectiveQuestionService {

    @Autowired
    private ExamDao examDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ObjectiveQuestionDao objectiveQuestionDao;

    /**
     * 添加问答题
     *
     * @param examId  考试ID
     * @param subject 题目
     * @param score   分值
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String addObjectQuestion(String examId, String subject, String score, HttpServletRequest request) {
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
        } else if (StringUtils.isEmpty(score)) {
            content = "分值不能为空！";
        } else {
            boolean isInt = true;
            int scoreInt = 0;
            try {
                scoreInt = Integer.parseInt(score);
            } catch (Exception e) {
                isInt = false;
                content = "分数必须为整数";
            }
            if (isInt) {
                if (scoreInt < 0) {
                    content = "成绩不能为负数！";
                } else {
                    Exam exam = examDao.getExamById(examId);
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    if (exam.getStartTime() != null && !time.before(exam.getStartTime())) {
                        content = "考试已经开始，无法修改试题！";
                    } else if (exam.getEndTime() != null && !time.before(exam.getEndTime())) {
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
                                while (objectiveQuestionDao.getCountById(id) != 0) {
                                    long idLong = Long.parseLong(id);
                                    Random random = new Random();
                                    idLong += random.nextInt(100);
                                    id = idLong + "";
                                    if (id.length() > 17) {
                                        id = id.substring(0, 17);
                                    }
                                }
                                ObjectiveQuestion objectiveQuestion = new ObjectiveQuestion();
                                objectiveQuestion.setId(id);
                                objectiveQuestion.setSubject(subject);
                                objectiveQuestion.setScore(scoreInt);
                                objectiveQuestion.setCreateTime(time);
                                objectiveQuestion.setExamId(examId);
                                try {
                                    if (objectiveQuestionDao.addObjectiveQuestion(objectiveQuestion) == 0) {
                                        System.out.println("ERROR:为考试" + exam.toString() + "添加问答题" + objectiveQuestion.toString() + "操作数据库失败");
                                        content = "操作数据库失败！";
                                    } else {
                                        status = "true";
                                        content = "添加成功！";
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:为考试" + exam.toString() + "添加问答题" + objectiveQuestion.toString() + "操作数据库失败，失败原因：" + e);
                                    content = "操作数据库失败！";
                                }
                            } else {
                                content = "不可以为别人的课程添加试题！";
                            }
                            if ("true".equals(status)) {
                                exam.setModifyTime(time);
                                course.setModifyTime(time);
                                try {
                                    if (examDao.updateExam(exam) == 0) {
                                        System.out.println("ERROR:添加问答题成功后修改考试" + exam.toString() + "操作数据库失败");
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:添加问答题成功后修改考试" + exam.toString() + "操作数据库失败，失败原因：" + e);
                                }
                                try {
                                    if (courseDao.editCourse(course) == 0) {
                                        System.out.println("ERROR:添加问答题成功后修改课程" + course.toString() + "操作数据库失败");
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:添加问答题成功后修改课程" + course.toString() + "操作数据库失败，失败原因：" + e);
                                }
                            }
                        }
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }


    /**
     * 根据考试ID获取客观题信息
     *
     * @param examId 考试ID
     * @return 客观题信息
     * @author 郭欣光
     */
    @Override
    public List<ObjectiveQuestion> getObjectQuestionByExamId(String examId) {
        if (objectiveQuestionDao.getCountByExamId(examId) == 0) {
            return null;
        } else {
            List<ObjectiveQuestion> objectiveQuestionList = objectiveQuestionDao.getObjectiveQuestionByExamId(examId);
            return objectiveQuestionList;
        }
    }

    /**
     * 根据客观题ID删除客观题
     *
     * @param objectiveQuestionId 客观题ID
     * @param request             用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteObjectiveQuestion(String objectiveQuestionId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (objectiveQuestionDao.getCountById(objectiveQuestionId) == 0) {
            content = "系统获取客观题题信息失败！";
        } else {
            ObjectiveQuestion objectiveQuestion = objectiveQuestionDao.getObjectiveQuestionById(objectiveQuestionId);
            if (examDao.getCountById(objectiveQuestion.getExamId()) == 0) {
                content = "系统获取考试信息失败！";
            } else {
                Exam exam = examDao.getExamById(objectiveQuestion.getExamId());
                if (courseDao.getCountById(exam.getCourseId()) == 0) {
                    content = "系统获取课程信息失败！";
                } else {
                    Course course = courseDao.getCourseById(exam.getCourseId());
                    User user = (User)session.getAttribute("user");
                    if (user.getEmail().equals(course.getUserEmail())) {
                        try {
                            if (objectiveQuestionDao.deleteObjectiveQuestion(objectiveQuestion) == 0) {
                                content = "删除客观题时操作数据库失败！";
                                System.out.println("ERROR:删除客观题" + objectiveQuestion.toString() + "时操作数据库失败");
                            } else {
                                status = "true";
                                content = "删除成功！";
                            }
                        } catch (Exception e) {
                            content = "删除客观题时操作数据库失败！";
                            System.out.println("ERROR:删除客观题" + objectiveQuestion.toString() + "时操作数据库失败，失败原因：" + e);
                        }
                    } else {
                        content = "抱歉，您没有权限修改他人的考试试题！";
                    }
                    if ("true".equals(status)) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        exam.setModifyTime(time);
                        course.setModifyTime(time);
                        try {
                            if (examDao.updateExam(exam) == 0) {
                                System.out.println("ERROR:删除客观题题成功后修改考试" + exam.toString() + "操作数据库失败");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:删除客观题题成功后修改考试" + exam.toString() + "操作数据库失败，失败原因：" + e);
                        }
                        try {
                            if (courseDao.editCourse(course) == 0) {
                                System.out.println("ERROR:删除客观题题成功后修改课程" + course.toString() + "操作数据库失败");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:删除客观题题成功后修改课程" + course.toString() + "操作数据库失败，失败原因：" + e);
                        }
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}
