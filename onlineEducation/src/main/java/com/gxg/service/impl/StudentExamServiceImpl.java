package com.gxg.service.impl;

import com.gxg.dao.ExamDao;
import com.gxg.dao.StudentExamDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Exam;
import com.gxg.entities.StudentExam;
import com.gxg.entities.User;
import com.gxg.service.StudentExamService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 学生考试相关业务处理
 * @author 郭欣光
 * @date 2019/4/13 14:47
 */

@Service(value = "studentExamService")
public class StudentExamServiceImpl implements StudentExamService {

    @Autowired
    private ExamDao examDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudentExamDao studentExamDao;

    @Value("${student.exam.count.each.page}")
    private int studentExamCountEachPage;

    /**
     * 获得学生考试相关信息（如果没有则添加）
     *
     * @param examId    考试ID
     * @param userEmail 用户邮箱
     * @return 学生考试信息（如果添加失败则为null）
     * @author 郭欣光
     */
    @Override
    public StudentExam getSetStudentExam(String examId, String userEmail) {
        if (examDao.getCountById(examId) == 0 || userDao.getUserCountByEmail(userEmail) == 0) {
            return null;
        } else {
            StudentExam studentExam = null;
            if (studentExamDao.getCountByExamIdAndUserEmail(examId, userEmail) == 0) {
                Exam exam = examDao.getExamById(examId);
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String timeString = time.toString();
                String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                while (studentExamDao.getCountById(id) != 0) {
                    long idLong = Long.parseLong(id);
                    Random random = new Random();
                    idLong += random.nextInt(100);
                    id = idLong + "";
                    if (id.length() > 17) {
                        id = id.substring(0, 17);
                    }
                }
                studentExam = new StudentExam();
                studentExam.setId(id);
                studentExam.setExamId(examId);
                studentExam.setUserEmail(userEmail);
                studentExam.setScore(-1);
                int duration = exam.getDuration();
                if (duration == 0) {
                    studentExam.setTime("0");
                } else {
                    studentExam.setTime(duration + ":00");
                }
                studentExam.setCreateTime(time);
                try {
                    if (studentExamDao.addStudentExam(studentExam) == 0) {
                        System.out.println("ERROR:系统在保存学生考试信息" + studentExam.toString() + "时操作数据库出错");
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:系统在保存学生考试信息" + studentExam.toString() + "时操作数据库出错，错误原因：" + e);
                    return null;
                }
            } else {
                List<StudentExam> studentExamList = studentExamDao.getStudentExamByExamIdAndUserEmail(examId, userEmail);
                studentExam = studentExamList.get(0);
            }
            return studentExam;
        }
    }

    /**
    * 更新考试剩余时间
    * @param examId 考试ID
    * @param examTime 考试剩余时间
    * @param request 用户请求相关信息
    * @return 处理结果
    * @author 郭欣光
    */
    @Override
    public String setStudentExamTime(String examId, String examTime, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "更新失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到用户登录信息！";
        } else {
            User user = (User)session.getAttribute("user");
            if (studentExamDao.getCountByExamIdAndUserEmail(examId, user.getEmail()) == 0) {
                content = "未检测到用户考试信息！";
            } else {
                try {
                    if (studentExamDao.updateStudentExamTimeByExamIdAndUserEmail(examTime, examId, user.getEmail()) == 0) {
                        System.out.println("ERROR:用户更新考试剩余时长操作数据库出错");
                        content = "操作数据库出错！";
                    } else {
                        status = "true";
                        content = "更新成功！";
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:用户更新考试剩余时长操作数据库出错，错误原因：" + e);
                    content = "操作数据库出错！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 获取指定考试ID指定页数的学生考试信息
     *
     * @param examId 考试ID
     * @param page   页数
     * @return 学生考试信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getStudentExamListByExamId(String examId, String page) {
        JSONObject result = new JSONObject();
        String status = "false";
        int studentExamPageNumber = 1;
        int studentExamPageInt = 0;
        String hasStudentExam = "false";
        try {
            studentExamPageInt = Integer.parseInt(page);
        } catch (Exception e) {
            studentExamPageInt = 0;
        }
        List<StudentExam> studentExamList = null;
        if (studentExamPageInt > 0) {
            int studentExamCount = studentExamDao.getCountByExamId(examId);
            if (studentExamCount != 0) {
                studentExamPageNumber = ((studentExamCount % studentExamCountEachPage) == 0) ? (studentExamCount / studentExamCountEachPage) : (studentExamCount / studentExamCountEachPage + 1);
                if (studentExamPageInt <= studentExamPageNumber) {
                    status = "true";
                    hasStudentExam = "true";
                    studentExamList = studentExamDao.getStudentExamByExamIdAndLimitOrderByCreateTime(examId, studentExamPageInt - 1, studentExamCountEachPage);
                    for (StudentExam studentExam : studentExamList) {
                        if (userDao.getUserCountByEmail(studentExam.getUserEmail()) != 0) {
                            User user = userDao.getUserByEmail(studentExam.getUserEmail());
                            studentExam.setStudentName(user.getName());
                        }
                    }
                }
            } else if (studentExamPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("studentExamPageNumber", studentExamPageNumber);
        result.accumulate("studentExamPage", studentExamPageInt);
        result.accumulate("studentExamList", studentExamList);
        result.accumulate("hasStudentExam", hasStudentExam);
        return result;
    }

    /**
     * 根据学生考试ID获取学生考试信息
     *
     * @param studentExamId 学生考试ID
     * @return 学生考试信息
     */
    @Override
    public StudentExam getStudentExamById(String studentExamId) {
        if (studentExamDao.getCountById(studentExamId) == 0) {
            return null;
        } else {
            StudentExam studentExam = studentExamDao.getStudentExamById(studentExamId);
            if (userDao.getUserCountByEmail(studentExam.getUserEmail()) != 0) {
                User user = userDao.getUserByEmail(studentExam.getUserEmail());
                studentExam.setStudentName(user.getName());
            }
            return studentExam;
        }
    }
}
