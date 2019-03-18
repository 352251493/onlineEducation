package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.UserDao;
import com.gxg.dao.UserStudyDao;
import com.gxg.entities.Course;
import com.gxg.entities.User;
import com.gxg.entities.UserStudy;
import com.gxg.service.MessageService;
import com.gxg.service.UserStudyService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 用户学习信息相关业务处理
 * @author 郭欣光
 * @date 2019/3/7 10:50
 */

@Service(value = "userStudyService")
public class UserStudyServiceImpl implements UserStudyService {

    @Autowired
    private UserStudyDao userStudyDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private MessageService messageService;

    @Value("${course.count.each.page}")
    private int courseCountEachPage;

    @Autowired
    private UserDao userDao;

    @Value("${sys.root.url}")
    private String sysRootUrl;

    /**
     * 添加公有课程用户学习信息（如果已存在该用户学习该课程的信息，则更新时间即可）
     *
     * @param user   用户信息
     * @param course 课程信息
     * @author 郭欣光
     */
    @Override
    public void addPublicUserStudy(User user, Course course) {
        if (!user.getEmail().equals(course.getUserEmail())) {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            boolean updateSuccess = false;
            if (userStudyDao.getCountByUserEmailAndCourseId(user.getEmail(), course.getId()) == 0) {
                String timeString = time.toString();
                String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                while (userStudyDao.getCountById(id) != 0) {
                    long idLong = Long.parseLong(id);
                    Random random = new Random();
                    idLong += random.nextInt(100);
                    id = idLong + "";
                    if (id.length() > 17) {
                        id = id.substring(0, 17);
                    }
                }
                UserStudy userStudy = new UserStudy();
                userStudy.setId(id);
                userStudy.setUserEmail(user.getEmail());
                userStudy.setCourseId(course.getId());
                userStudy.setIsPrivate("0");
                userStudy.setCreateTime(time);
                userStudy.setLastStudyTime(time);
                try {
                    if (userStudyDao.createUserStudy(userStudy) == 0) {
                        System.out.println("ERROR:添加用户学习信息" + userStudy.toString() + "操作数据库失败");
                    } else {
                        updateSuccess = true;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:添加用户学习信息" + userStudy.toString() + "操作数据库失败，失败原因：" + e);
                }
                if (updateSuccess) {
                    String messageTitle = "学习一门新课程";
                    String messageContent = createAddPublicUserStudySuccessEmailMessage(course);
                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                    System.out.println("INFO:用户学习信息" + userStudy.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                }
            } else {
                List<UserStudy> userStudyList = userStudyDao.getUserStudyByUserEmailAndCourseId(user.getEmail(), course.getId());
                UserStudy userStudy = userStudyList.get(0);
                userStudy.setLastStudyTime(time);
                try {
                    if (userStudyDao.updateUserStudy(userStudy) == 0) {
                        System.out.println("ERROR:更新用户学习信息" + userStudy.toString() + "操作数据库失败");
                    } else {
                        updateSuccess = true;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:更新用户学习信息" + userStudy.toString() + "操作数据库失败，失败原因：" + e);
                }
            }
            if (updateSuccess) {
                int studyNumber = userStudyDao.getCountByCourseId(course.getId());
                if (courseDao.getCountById(course.getId()) != 0) {
                    Course course1 = courseDao.getCourseById(course.getId());
                    if (course1.getStudyNumber() != studyNumber) {
                        try {
                            if (courseDao.updateStudyNumberById(studyNumber, course.getId()) == 0) {
                                System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败，失败原因：" + e);
                        }
                    }
                }
            }
        }
    }

    private String createAddPublicUserStudySuccessEmailMessage(Course course) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>太棒啦！您于" + timeString + "开始学习新课程：" + course.getName() + "&nbsp;&nbsp;您可以在<b>个人中心-我的课程-公共课程</b>中查看您学习的课程信息！</p>";
        message += "<p>您可直接点击：<a href='" + sysRootUrl + "/course/public/detail/" +course.getId() + "/1'>" + course.getName() + "</a>进入该课程学习</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    /**
     * 根据用户信息及课程类型获取指定页数用户学习课程信息
     * @param user 用户信息
     * @param isPrivate 课程类型
     * @param coursePage 指定页数
     * @return 课程信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getStudyCourseByUserAndIsPrivate(User user, String isPrivate, String coursePage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int coursePageNumber = 1;
        int coursePageInt = 0;
        String hasCourse = "false";
        try {
            coursePageInt = Integer.parseInt(coursePage);
        } catch (Exception e) {
            coursePageInt = 0;
        }
        List<Course> courseList = null;
        if (coursePageInt > 0) {
            int courseCount = userStudyDao.getCountByUserEmailAndIsPrivate(user.getEmail(), isPrivate);
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    List<UserStudy> userStudyList = userStudyDao.getUserStudyByUserEmailAndIsPrivateAndLimitOrderByLastStudyTime(user.getEmail(), isPrivate, coursePageInt - 1, courseCountEachPage);
                    courseList = new ArrayList<Course>();
                    for (UserStudy userStudy : userStudyList) {
                        Course course = courseDao.getCourseById(userStudy.getCourseId());
                        courseList.add(course);
                    }
                    for (Course course : courseList) {
                        if (userDao.getUserCountByEmail(course.getUserEmail()) != 0) {
                            User user1 = userDao.getUserByEmail(course.getUserEmail());
                            course.setUserName(user1.getName());
                        }
                    }
                }
            } else if (coursePageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("coursePageNumber", coursePageNumber);
        result.accumulate("coursePage", coursePageInt);
        result.accumulate("courseList", courseList);
        result.accumulate("hasCourse", hasCourse);
        return result;
    }


    /**
     * 根据课程ID获取用户学习信息
     *
     * @param courseId 课程ID
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyListByCourseId(String courseId) {
        if (userStudyDao.getCountByCourseId(courseId) == 0) {
            return null;
        }
        List<UserStudy> userStudyList = userStudyDao.getUserStudyByCourseIdOrderByCreateTime(courseId);
        for (UserStudy userStudy : userStudyList) {
            if (userStudy.getUserEmail() != null && userDao.getUserCountByEmail(userStudy.getUserEmail()) != 0) {
                User user = userDao.getUserByEmail(userStudy.getUserEmail());
                userStudy.setUserName(user.getName());
            }
        }
        return userStudyList;
    }

    /**
     * 添加用户学习信息
     *
     * @param courseId       课程ID
     * @param userStudyEmail 添加用户邮箱
     * @param request        用户请求信息
     * @return 处理结果
     */
    @Override
    public String addUserStudy(String courseId, String userStudyEmail, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (courseDao.getCountById(courseId) == 0) {
            content = "系统未检测到课程信息！";
        } else if (userDao.getUserCountByEmail(userStudyEmail) == 0) {
            content = "该用户不存在！";
        } else if (userStudyDao.getCountByUserEmailAndCourseId(userStudyEmail, courseId) != 0) {
            content = "该用户已在该课程中！";
        } else {
            Course course = courseDao.getCourseById(courseId);
            User user = (User)session.getAttribute("user");
            if (user.getEmail().equals(userStudyEmail)) {
                content = "您不能将自己加入该课程！";
            } else {
                if ("1".equals(course.getIsPrivate())) {
                    if (user.getEmail().equals(course.getUserEmail())) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String timeString = time.toString();
                        String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                        while (userStudyDao.getCountById(id) != 0) {
                            long idLong = Long.parseLong(id);
                            Random random = new Random();
                            idLong += random.nextInt(100);
                            id = idLong + "";
                            if (id.length() > 17) {
                                id = id.substring(0, 17);
                            }
                        }
                        UserStudy userStudy = new UserStudy();
                        userStudy.setId(id);
                        userStudy.setUserEmail(userStudyEmail);
                        userStudy.setCourseId(courseId);
                        userStudy.setIsPrivate(course.getIsPrivate());
                        userStudy.setCreateTime(time);
                        userStudy.setLastStudyTime(time);
                        try {
                            if (userStudyDao.createUserStudy(userStudy) == 0) {
                                System.out.println("ERROR:添加用户学习信息" + userStudy.toString() + "操作数据库失败");
                            } else {
                                status = "true";
                                content = "添加成功！";
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:添加用户学习信息" + userStudy.toString() + "操作数据库失败，失败原因：" + e);
                        }
                        if ("true".equals(status)) {
                            int studyNumber = userStudyDao.getCountByCourseId(courseId);
                            if (courseDao.getCountById(courseId) != 0) {
                                Course course1 = courseDao.getCourseById(courseId);
                                if (course1.getStudyNumber() != studyNumber) {
                                    try {
                                        if (courseDao.updateStudyNumberById(studyNumber, course1.getId()) == 0) {
                                            System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败，失败原因：" + e);
                                    }
                                }
                            }
                            User user1 = userDao.getUserByEmail(userStudyEmail);
                            String messageTitle = "添加学习人员成功";
                            String messageContent = createAddUserStudySuccessTeacherEmailMessage(course, user1);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:用户学习信息" + userStudy.toString() + "创建成功时创建教师消息通知结果：" + createMessageResult.toString());
                            messageTitle = "您有一门新的私人课程";
                            messageContent = createAddUserStudySuccessStudentEmailMessage(course, user);
                            createMessageResult = messageService.createMessage(user1.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:用户学习信息" + userStudy.toString() + "创建成功时创建学生消息通知结果：" + createMessageResult.toString());
                        }
                    } else {
                        content = "抱歉，您没有权限为他人的课程进行添加用学习人员！";
                    }
                } else {
                    content = "该课程为公开课程，仅有私有课程需要添加学习人员信息！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }


    private String createAddUserStudySuccessTeacherEmailMessage(Course course, User user) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>恭喜您！您于" + timeString + "成功为您的私有课程&nbsp;&nbsp:" + course.getName() + "&nbsp;&nbsp;添加学习人员：" + user.getName() + "</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    private String createAddUserStudySuccessStudentEmailMessage(Course course, User user) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>" + user.getName() + "于" + timeString + "将您加入私人课程：" + course.getName() + "&nbsp;&nbsp;您可以在<b>个人中心-我的课程-私人课程</b>中查看您的课程信息！</p>";
        message += "<p>您可直接点击：<a href='" + sysRootUrl + "/course/private/detail/" +course.getId() + "/1'>" + course.getName() + "</a>进入该课程学习</p>";
        message += "<p style='color: red;'>如果不是您不想学习该课程，您可以忽略该条信息！</p>";
        return message;
    }


    /**
     * 删除用户学习信息
     *
     * @param userStudyId 用户学习信息ID
     * @param request     用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteUserStudy(String userStudyId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (userStudyDao.getCountById(userStudyId) == 0) {
            content = "系统未获得该用户学习信息！";
        } else {
            UserStudy userStudy = userStudyDao.getUserStudyById(userStudyId);
            if (courseDao.getCountById(userStudy.getCourseId()) == 0) {
                content = "系统未检测到对应的课程信息！";
            } else {
                Course course = courseDao.getCourseById(userStudy.getCourseId());
                User user = (User)session.getAttribute("user");
                if (user.getEmail().equals(course.getUserEmail())) {
                    if ("1".equals(course.getIsPrivate())) {
                        try {
                            if (userStudyDao.deleteUserStudy(userStudy) == 0) {
                                content = "删除用户学习信息操作数据库失败！";
                            } else {
                                status = "true";
                                content = "删除成功！";
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:删除用户学习信息" + userStudy.toString() + "操作数据库失败，失败原因：" + e);
                            content = "删除用户学习信息操作数据库失败！";
                        }
                    } else {
                        content = "该课程为公开课程，仅有私有课程才可以删除学习人员信息！";
                    }
                } else {
                    content = "抱歉，您没有权限删除他人课程的学习人员！";
                }

                if ("true".equals(status)) {
                    int studyNumber = userStudyDao.getCountByCourseId(course.getId());
                    if (courseDao.getCountById(course.getId()) != 0) {
                        Course course1 = courseDao.getCourseById(course.getId());
                        if (course1.getStudyNumber() != studyNumber) {
                            try {
                                if (courseDao.updateStudyNumberById(studyNumber, course1.getId()) == 0) {
                                    System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:更新课程" + course1.toString() + "学习人数操作数据库失败，失败原因：" + e);
                            }
                        }
                    }
                    if (userDao.getUserCountByEmail(userStudy.getUserEmail()) != 0) {
                        User user1 = userDao.getUserByEmail(userStudy.getUserEmail());
                        String messageTitle = "删除学习人员成功";
                        String messageContent = createDeleteUserStudySuccessTeacherEmailMessage(course, user1);
                        JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                        System.out.println("INFO:用户学习信息" + userStudy.toString() + "删除成功时创建教师消息通知结果：" + createMessageResult.toString());
                        messageTitle = "您有一门课程被移除";
                        messageContent = createDeleteUserStudySuccessStudentEmailMessage(course, user);
                        createMessageResult = messageService.createMessage(user1.getEmail(), messageTitle, messageContent);
                        System.out.println("INFO:用户学习信息" + userStudy.toString() + "删除成功时创建学生消息通知结果：" + createMessageResult.toString());
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteUserStudySuccessTeacherEmailMessage(Course course, User user) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>您于" + timeString + "将" + user.getName() + "移除您的私有课程&nbsp;&nbsp:" + course.getName() + "</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    private String createDeleteUserStudySuccessStudentEmailMessage(Course course, User user) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>" + user.getName() + "于" + timeString + "将您移出课程：" + course.getName() + "</p>";
        message += "<p style='color: red;'>如果您急切希望学习该课程，请私下联系该课程的教师</p>";
        return message;
    }


    /**
     * 根据课程ID及用户邮箱获取用户学习信息
     *
     * @param courseId  课程ID
     * @param userEmail 用户邮箱
     * @return 用户学习信息
     * @author 郭欣光
     */
    @Override
    public List<UserStudy> getUserStudyByCourseIdAndUserEmail(String courseId, String userEmail) {
        if (userStudyDao.getCountByUserEmailAndCourseId(userEmail, courseId) == 0) {
            return null;
        } else {
            List<UserStudy> userStudyList = userStudyDao.getUserStudyByUserEmailAndCourseId(userEmail, courseId);
            return userStudyList;
        }
    }
}
