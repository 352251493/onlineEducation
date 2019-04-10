package com.gxg.service.impl;

import com.gxg.dao.*;
import com.gxg.entities.*;
import com.gxg.service.ExamService;
import com.gxg.service.MessageService;
import com.gxg.utils.StringUtils;
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
 * 考试相关业务处理
 * @author 郭欣光
 * @date 2019/3/21 10:52
 */
@Service(value = "examService")
public class ExamServiceImpl implements ExamService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ExamDao examDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserStudyDao userStudyDao;

    @Autowired
    private UserDao userDao;

    @Value("${exam.count.each.page}")
    private int examCountEachPage;

    @Autowired
    private ChoiceQuestionDao choiceQuestionDao;

    @Autowired
    private ObjectiveQuestionDao objectiveQuestionDao;

    @Value("${sys.root.url}")
    private String sysRootUrl;

    /**
     * 创建考试
     *
     * @param courseId        课程ID
     * @param examName        考试名称
     * @param examRequirement 考试要求
     * @param examStartTime   考试最早考试时间
     * @param examEndTime     考试最晚结束时间
     * @param examDuration    考试时长
     * @param request         用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createExam(String courseId, String examName, String examRequirement, String examStartTime, String examEndTime, String examDuration, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (courseDao.getCountById(courseId) == 0) {
            content = "系统未检测到课程信息";
        } else if (StringUtils.isEmpty(examName)) {
            content = "考试名称不能为空！";
        } else if (examName.length() > 100) {
            content = "考试名称不能超过100字节！";
        } else if (StringUtils.isEmpty(examRequirement)) {
            content = "考试要求不能为空！";
        } else if (examRequirement.length() > 1000) {
            content = "考试要求不能超过1000字符！";
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseDao.getCourseById(courseId);
            if (user.getEmail().equals(course.getUserEmail())) {
                Timestamp endTime = null;
                Timestamp startTime = null;
                Timestamp time = new Timestamp(System.currentTimeMillis());
                boolean isSuccess = true;
                int duration = 0;
                if (!StringUtils.isEmpty(examStartTime)) {
                    try {
                        startTime = Timestamp.valueOf(examStartTime);
                        if (!time.before(startTime)) {
                            content = "开始时间不能早于当前时间！";
                            isSuccess = false;
                        }
                    } catch (Exception e) {
                        content = "开始时间不是时间类型！";
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
                    if (!StringUtils.isEmpty(examEndTime)) {
                        try {
                            endTime = Timestamp.valueOf(examEndTime);
                        } catch (Exception e) {
                            content = "结束时间不是时间类型！";
                            isSuccess = false;
                        }
                        if (isSuccess) {
                            if (!time.before(endTime)) {
                                content = "结束时间不能早于当前时间！";
                                isSuccess = false;
                            } else if (startTime != null && endTime.before(startTime)) {
                                content = "结束时间不能早于开始时间";
                                isSuccess = false;
                            }
                        }
                    }
                    if (isSuccess) {
                        if (!StringUtils.isEmpty(examDuration)) {
                            try {
                                duration = Integer.parseInt(examDuration);
                                if (duration <= 0) {
                                    content = "考试时长必须为正整数！";
                                    isSuccess = false;
                                }
                            } catch (Exception e) {
                                content = "考试时长必须为正整数！";
                                isSuccess = false;
                            }
                        }
                        if (isSuccess) {
                            String timeString = time.toString();
                            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                            while (examDao.getCountById(id) != 0) {
                                long idLong = Long.parseLong(id);
                                Random random = new Random();
                                idLong += random.nextInt(100);
                                id = idLong + "";
                                if (id.length() > 17) {
                                    id = id.substring(0, 17);
                                }
                            }
                            Exam exam = new Exam();
                            exam.setId(id);
                            exam.setName(examName);
                            exam.setRequirement(examRequirement);
                            exam.setStartTime(startTime);
                            exam.setEndTime(endTime);
                            exam.setDuration(duration);
                            exam.setCreateTime(time);
                            exam.setModifyTime(time);
                            exam.setCourseId(courseId);
                            try {
                                if (examDao.createExam(exam) == 0) {
                                    content = "操作数据库失败！";
                                    System.out.println("ERROR:创建考试" + exam.toString() + "操作数据库失败");
                                } else {
                                    status = "true";
                                    content = id;
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:创建考试" + exam.toString() + "操作数据库失败，失败原因：" + e);
                                content = "操作数据库失败！";
                            }
                            if ("true".equals(status)) {
                                String messageTitle = "您已成功创建考试";
                                String messageContent = createCreateExamSuccessTeacherEmailMessage(course, exam);
                                JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                                System.out.println("INFO:考试" + exam.toString() + "创建成功时创建教师消息通知结果：" + createMessageResult.toString());
                                if (userStudyDao.getCountByCourseId(courseId) != 0) {
                                    List<UserStudy> userStudyList = userStudyDao.getUserStudyByCourseIdOrderByCreateTime(courseId);
                                    for (UserStudy userStudy : userStudyList) {
                                        if (userDao.getUserCountByEmail(userStudy.getUserEmail()) != 0) {
                                            User user1 = userDao.getUserByEmail(userStudy.getUserEmail());
                                            messageTitle = "您有一门新考试";
                                            messageContent = createCreateExamSuccessStudentEmailMessage(course, exam, user);
                                            createMessageResult = messageService.createMessage(user1.getEmail(), messageTitle, messageContent);
                                            System.out.println("INFO:考试" + exam.toString() + "创建成功时创建学生" + user1.toString() + "消息通知结果：" + createMessageResult.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                content = "只可以为自己创建的课程添加考试！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateExamSuccessTeacherEmailMessage(Course course, Exam exam) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "成功为课程：" + course.getName() + "&nbsp;&nbsp;创建考试：" + exam.getName() + "！</p>";
        message += "<p>我们将自动为学习该课程的用户发送提醒，为了保证所有人都收到该提醒，您也可自行通知！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    private String createCreateExamSuccessStudentEmailMessage(Course course, Exam exam, User user) {
        String courseType = "public";
        if ("1".equals(course.getIsPrivate())) {
            courseType = "private";
        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>您所学习的课程：" + course.getName() + "&nbsp;&nbsp;由" + user.getName() + "于" + timeString + "创建考试：" + exam.getName() + "！</p>";
        message += "<p>考试要求：" + exam.getRequirement() + "</p>";
        if (exam.getStartTime() == null) {
            message += "<p>开始时间：无</p>";
        } else {
            message += "<p>开始时间：" + exam.getStartTime().toString().split("\\.")[0] + "</p>";
        }
        if (exam.getEndTime() == null) {
            message += "<p>结束时间：无</p>";
        } else {
            message += "<p>结束时间：" + exam.getEndTime().toString().split("\\.")[0] + "</p>";
        }
        if (exam.getDuration() == 0) {
            message += "<p>考试时长：无</p>";
        } else {
            message += "<p>考试时长：" + exam.getDuration() + "</p>";
        }
        message += "<p>您可以通过<b>我的课程-课程详情-考试列表</b>进行查看，或点击连接<a href='" + sysRootUrl + "exam/" + courseType + "/detail/" + exam.getId() + "'>" + sysRootUrl + "exam/" + courseType + "/detail/" + exam.getId() + "</a>进入考试";
        return message;
    }

    /**
     * 获取指定课程指定页数的考试信息
     *
     * @param courseId 课程ID
     * @param examPage 页数
     * @return 考试先关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getExamListByCourseId(String courseId, String examPage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int examPageNumber = 1;
        int examPageInt = 0;
        String hasExam = "false";
        try {
            examPageInt = Integer.parseInt(examPage);
        } catch (Exception e) {
            examPageInt = 0;
        }
        List<Exam> examList = null;
        if (examPageInt > 0) {
            int examCount = examDao.getCountByCourseId(courseId);
            if (examCount != 0) {
                examPageNumber = ((examCount % examCountEachPage) == 0) ? (examCount / examCountEachPage) : (examCount / examCountEachPage + 1);
                if (examPageInt <= examPageNumber) {
                    status = "true";
                    hasExam = "true";
                    examList = examDao.getExamByCourseIdAndLimitOrderByModifyTime(courseId, examPageInt - 1, examCountEachPage);
                }
            } else if (examPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("examPageNumber", examPageNumber);
        result.accumulate("examPage", examPageInt);
        result.accumulate("examList", examList);
        result.accumulate("hasExam", hasExam);
        return result;
    }

    /**
     * 根据考试ID获取考试信息
     *
     * @param examId 考试ID
     * @return 考试信息
     * @author 郭欣光
     */
    @Override
    public Exam getExamById(String examId) {
        if (examDao.getCountById(examId) == 0) {
            return null;
        } else {
            Exam exam = examDao.getExamById(examId);
            return exam;
        }
    }

    /**
     * 获取指定课程ID的前N个考试信息
     *
     * @param courseId  课程ID
     * @param topNumber N
     * @return 考试信息
     * @author 郭欣光
     */
    @Override
    public List<Exam> getExamListByCourseIdAndTopNumber(String courseId, int topNumber) {
        if (examDao.getCountByCourseId(courseId) == 0) {
            return null;
        } else {
            List<Exam> examList = examDao.getExamByCourseIdAndLimitOrderByModifyTime(courseId, 0, topNumber);
            return examList;
        }
    }

    /**
     * 删除考试信息
     *
     * @param examId  考试ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteExam(String examId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (examDao.getCountById(examId) == 0) {
            content = "系统获取考试信息失败！";
        } else {
            Exam exam = examDao.getExamById(examId);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            if (exam.getStartTime() != null && !time.before(exam.getStartTime())) {
                content = "考试已经开始，无法删除试题！";
            } else if (exam.getEndTime() != null && !time.before(exam.getEndTime())) {
                content = "开始已经结束，无法删除试题！";
            } else {
                if (courseDao.getCountById(exam.getCourseId()) == 0) {
                    content = "系统获取课程信息失败！";
                } else {
                    Course course = courseDao.getCourseById(exam.getCourseId());
                    User user = (User)session.getAttribute("user");
                    if (user.getEmail().equals(course.getUserEmail())) {
                        List<ChoiceQuestion> choiceQuestionList = null;
                        List<ObjectiveQuestion> objectiveQuestionList = null;
                        if (choiceQuestionDao.getCountByExamId(examId) != 0) {
                            choiceQuestionList = choiceQuestionDao.getChoiceQuestionByExamId(examId);
                        }
                        if (objectiveQuestionDao.getCountByExamId(examId) != 0) {
                            objectiveQuestionList = objectiveQuestionDao.getObjectiveQuestionByExamId(examId);
                        }
                        try {
                            if (examDao.deleteExam(exam) == 0) {
                                content = "删除考试时操作数据库失败！";
                                System.out.println("ERROR:删除考试" + exam.toString() + "时，操作数据库失败");
                            } else {
                                status = "true";
                                content = course.getId();
                            }
                        } catch (Exception e) {
                            content = "删除考试时操作数据库失败！";
                            System.out.println("ERROR:删除考试" + exam.toString() + "时，操作数据库失败，失败原因：" + e);
                        }
                        if ("true".equals(status)) {
                            course.setModifyTime(time);
                            try {
                                if (courseDao.editCourse(course) == 0) {
                                    System.out.println("ERROR:删除考试成功后修改课程" + course.toString() + "操作数据库失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:删除考试成功后修改课程" + course.toString() + "操作数据库失败，失败原因：" + e);
                            }
                            if (choiceQuestionList != null) {
                                for (ChoiceQuestion choiceQuestion : choiceQuestionList) {
                                    if (choiceQuestionDao.getCountById(choiceQuestion.getId()) != 0) {
                                        try {
                                            if (choiceQuestionDao.deleteChoiceQuestion(choiceQuestion) == 0) {
                                                System.out.println("ERROR:删除考试" + exam.toString() + "成功后，删除选择题" + choiceQuestion.toString() + "操作数据库失败");
                                            }
                                        } catch (Exception e) {
                                            System.out.println("ERROR:删除考试" + exam.toString() + "成功后，删除选择题" + choiceQuestion.toString() + "操作数据库失败，失败原因：" + e);
                                        }
                                    }
                                }
                            }

                            if (objectiveQuestionList != null) {
                                for (ObjectiveQuestion objectiveQuestion : objectiveQuestionList) {
                                    if (objectiveQuestionDao.getCountById(objectiveQuestion.getId()) != 0) {
                                        try {
                                            if (objectiveQuestionDao.deleteObjectiveQuestion(objectiveQuestion) == 0) {
                                                System.out.println("ERROR:删除考试" + exam.toString() + "成功后，删除客观题" + objectiveQuestion.toString() + "操作数据库失败");
                                            }
                                        } catch (Exception e) {
                                            System.out.println("ERROR:删除考试" + exam.toString() + "成功后，删除客观题" + objectiveQuestion.toString() + "操作数据库失败，失败原因：" + e);
                                        }
                                    }
                                }
                            }

                            String messageTitle = "您已成功删除考试";
                            String messageContent = createDeleteExamSuccessTeacherEmailMessage(exam);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:考试" + exam.toString() + "删除成功时创建教师消息通知结果：" + createMessageResult.toString());
                            if (userStudyDao.getCountByCourseId(course.getId()) != 0) {
                                List<UserStudy> userStudyList = userStudyDao.getUserStudyByCourseIdOrderByCreateTime(course.getId());
                                for (UserStudy userStudy : userStudyList) {
                                    if (userDao.getUserCountByEmail(userStudy.getUserEmail()) != 0) {
                                        User user1 = userDao.getUserByEmail(userStudy.getUserEmail());
                                        messageTitle = "您有一门考试被取消";
                                        messageContent = createDeleteExamSuccessStudentEmailMessage(course, exam, user);
                                        createMessageResult = messageService.createMessage(user1.getEmail(), messageTitle, messageContent);
                                        System.out.println("INFO:考试" + exam.toString() + "删除成功时创建学生" + user1.toString() + "消息通知结果：" + createMessageResult.toString());
                                    }
                                }
                            }

                        }
                    } else {
                        content = "抱歉，您没有权限修改他人的考试试题！";
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteExamSuccessTeacherEmailMessage(Exam exam) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "成功删除考试" + exam.getName() + "&nbsp;&nbsp;！</p>";
        message += "<p>我们将自动为学习该课程的用户发送提醒，为了保证所有人都收到该提醒，您也可自行通知！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    private String createDeleteExamSuccessStudentEmailMessage(Course course, Exam exam, User user) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>您学习的课程" + course.getName() + "&nbsp;&nbsp;由" + user.getName() + "于" + timeString + "删除考试" + exam.getName() + "&nbsp;&nbsp;！</p>";
        message += "<p>愿考试连接已经作废，如有疑问请自行联系该门课程的教师！</p>";
        return message;
    }
}
