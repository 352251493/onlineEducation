package com.gxg.service.impl;

import com.gxg.dao.*;
import com.gxg.entities.*;
import com.gxg.service.CourseService;
import com.gxg.service.MessageService;
import com.gxg.utils.FileUtils;
import com.gxg.utils.Md5;
import com.gxg.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 课程相关业务处理
 * @author 郭欣光
 * @date 2019/1/30 14:35
 */

@Service(value = "courseService")
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Value("${course.count.each.page}")
    private int courseCountEachPage;

    @Autowired
    private UserDao userDao;

    @Value("${course.image.max.size}")
    private long courseImageMaxSize;

    @Value("${course.image.max.size.string}")
    private String courseImageMaxSizeString;

    @Value("${course.resource.dir}")
    private String courseResourceDir;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private LessonDataDao lessonDataDao;

    /**
     * 根据指定页数按照修改时间顺序获取课程列表
     *
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCourseListOrderByModifyTime(String coursePage) {
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
            int courseCount = courseDao.getCourseCountByIsPrivate("0");
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLimitAndIsPrivateOrderByModifyTime(coursePageInt - 1, courseCountEachPage, "0");
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
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
     * 根据指定页数按照学习人数顺序获取课程列表
     *
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCourseListOrderByStudyNumber(String coursePage) {
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
            int courseCount = courseDao.getCourseCountByIsPrivate("0");
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLimitAndIsPrivateOrderByStudyNumber(coursePageInt - 1, courseCountEachPage, "0");
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
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
     * 根据搜索内容和指定页数获取课程列表
     * @param searchContent 搜索内容
     * @param coursePage 当前页数
     * @return 课程列表相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject searchCourse(String searchContent, String coursePage) {
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
            int courseCount = courseDao.getCountByLikeNameOrLikeUserNameAndIsPrivate(searchContent, "0");
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByLikeNameOrLikeUserNameAndLimitAndIsPrivateOrderByTime(searchContent, coursePageInt - 1, courseCountEachPage, "0");
                    for (Course course : courseList) {
                        User user = userDao.getUserByEmail(course.getUserEmail());
                        course.setUserName(user.getName());
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
     * 创建课程业务逻辑处理
     * @param courseName 课程名称
     * @param courseIntroduction 课程简介
     * @param courseImage 课程封面图片
     * @param isPrivate 是否私有
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createCourse(String courseName, String courseIntroduction, MultipartFile courseImage, String isPrivate, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "创建失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (StringUtils.isEmpty(courseName)) {
            content = "课程名称不能为空！";
        } else if (courseName.length() > 100) {
            content = "课程名称长度不能超过100字符！";
        } else if (StringUtils.isEmpty(courseIntroduction)) {
            content = "课程简介不能为空！";
        } else if (courseIntroduction.length() > 500) {
            content = "课程简介长度不能超过500字符！";
        } else if (StringUtils.isEmpty(isPrivate) || isPrivate.length() > 1) {
            content = "咦？难道课程类型被篡改了？";
        } else if (courseImage == null) {
            content = "为该课程选一个好看的封面吧~";
        } else {
            boolean isImage = true;
            try {
                if (!FileUtils.isImage(courseImage)) {
                    isImage = false;
                    content = "课程封面必须是图片类型哦~";
                }
            } catch (Exception e) {
                isImage = false;
                System.out.println("ERROR:用户在创建课程判断封面图片是否为图片类型时出错，错误原因：" + e);
                content = "系统在判断封面图片是否为图片类型时出错";
            }
            if (isImage) {
                String courseImageName = courseImage.getOriginalFilename();
                String courseImageType = courseImageName.substring(courseImageName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtils.isImageByType(courseImageType)) {
                    content = "课程封面必须是图片类型哦~";
                } else if (FileUtils.getFileSize(courseImage) > courseImageMaxSize) {
                    content = "图片太大啦，不要超过" + courseImageMaxSizeString + "哦~";
                } else {
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (courseDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    String idMd5 = Md5.md5(id);
                    String courseImageDir = courseResourceDir + idMd5 + "/";
                    String imageName = idMd5 + "." + courseImageType;
                    JSONObject uploadCourseImageResult = FileUtils.uploadFile(courseImage, imageName, courseImageDir);
                    if ("true".equals(uploadCourseImageResult.getString("status"))) {
                        Course course = new Course();
                        course.setId(id);
                        course.setName(courseName);
                        course.setIntroduction(courseIntroduction);
                        course.setImage(idMd5 + "/" + imageName);
                        course.setStudyNumber(0);
                        course.setCreateTime(time);
                        course.setModifyTime(time);
                        User user = (User)session.getAttribute("user");
                        course.setUserEmail(user.getEmail());
                        course.setIsPrivate(isPrivate);
                        boolean insertSuccess = false;
                        try {
                            if (courseDao.createCourse(course) == 0) {
                                System.out.println("ERROR:创建课程" + course.toString() + "操作数据库失败！");
                                content = "操作数据库失败！";
                            } else {
                                insertSuccess = true;
                                status = "true";
                                content = id;
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:创建课程" + course.toString() + "操作数据库出错，错误原因：" + e);
                            content = "操作数据库失败！";
                        }
                        if (insertSuccess) {
                            String messageTitle = "您已成功创建课程：" + courseName;
                            String messageContent = createCreateCourseSuccessEmailMessage(course);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:课程" + course.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                        } else {
                            JSONObject deleteCourseImageResult = FileUtils.deleteFile(courseImageDir + imageName);
                            System.out.println("INFO:创建课程操作数据库失败后删除课程封面图片结果：" + deleteCourseImageResult.toString());
                        }
                    } else {
                        System.out.println("ERROR:创建课程时上传封面图片失败，失败原因：" + uploadCourseImageResult.getString("content"));
                        content = "上传封面图片失败，失败原因" + uploadCourseImageResult.getString("content");
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateCourseSuccessEmailMessage( Course course) {
        String message = "<p>恭喜您，您的课程：" + course.getName() + "&nbsp;&nbsp;创建成功！";
        if ("0".equals(course.getIsPrivate())) {
            message += "您的课程为公开课程，将对所有人进行开放，感谢您的付出！</p>";
        } else {
            message += "您的课程为私有课程，您可以在该课程页面手动添加学习人员，仅有被添加人员才可以对该课程进行学习</p>";
        }
        message += "<p>想要让自己的课程更加精彩，快去添加内容吧！</p>";
        return message;
    }

    /**
     * 获取指定用户的前N个课程
     *
     * @param user      用户信息
     * @param topNumber 个数
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getUserCourseByTopNumber(User user, int topNumber) {
        if (courseDao.getCourseCountByUserEmail(user.getEmail()) != 0) {
            List<Course> courseList = courseDao.getCourseByUserEmailAndLimit(user.getEmail(), 0, topNumber);
            for (Course course : courseList) {
                course.setUserName(user.getName());
            }
            return courseList;
        } else {
            return null;
        }
    }

    /**
     * 根据ID获取课程信息
     *
     * @param courseId 课程ID
     * @return 课程信息
     * @author 郭欣光
     */
    @Override
    public Course getCourseById(String courseId) {
        if (courseDao.getCountById(courseId) == 0) {
            return null;
        } else {
            Course course = courseDao.getCourseById(courseId);
            User user = userDao.getUserByEmail(course.getUserEmail());
            course.setUserName(user.getName());
            return course;
        }
    }

    /**
     * 修改课程资料
     *
     * @param courseId           课程ID
     * @param courseName         课程名称
     * @param courseIntroduction 课程简介
     * @param request            用户请求信息
     * @return 修改结果
     * @author 郭欣光
     */
    @Override
    public String editCourse(String courseId, String courseName, String courseIntroduction, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "修改失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (StringUtils.isEmpty(courseName)) {
            content = "课程名称不能为空！";
        } else if (courseName.length() > 100) {
            content = "课程名称长度不能超过100字符！";
        } else if (StringUtils.isEmpty(courseIntroduction)) {
            content = "课程简介不能为空！";
        } else if (courseIntroduction.length() > 500) {
            content = "课程简介长度不能超过500字符！";
        } else if (courseDao.getCountById(courseId) == 0) {
            content = "该课程不存在！";
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseDao.getCourseById(courseId);
            if (course.getUserEmail().equals(user.getEmail())) {
                course.setName(courseName);
                course.setIntroduction(courseIntroduction);
                Timestamp time = new Timestamp(System.currentTimeMillis());
                course.setModifyTime(time);
                try {
                    if (courseDao.editCourse(course) == 0) {
                        System.out.println("ERROR:修改课程" + course.toString() + "时操作数据库失败！");
                        content = "操作数据库失败！";
                    } else {
                        status = "true";
                        content = "修改成功！";
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:修改课程" + course.toString() + "时操作数据库失败，失败原因：" + e);
                    content = "操作数据库失败！";
                }
            } else {
                content = "只可以修改自己创建的课程！";
            }
            if ("true".equals(status)) {
                String messageTitle = "您已成功修改课程：" + courseName;
                String messageContent = editCourseSuccessEmailMessage(course);
                JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                System.out.println("INFO:课程" + course.toString() + " 修改成功时创建消息通知结果：" + createMessageResult.toString());
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String editCourseSuccessEmailMessage(Course course) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>您于" + timeString + "修改课程:" + course.getName() + "&nbsp;&nbsp;资料成功，您可前往&nbsp;我的课程-我创建的&nbsp;进行查看</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能账号密码存在泄漏，请及时修改密码</p>";
        return message;
    }

    /**
     * 修改课程封面图片
     *
     * @param courseId    课程ID
     * @param courseImage 封面图片
     * @param request     用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String editCourseImage(String courseId, MultipartFile courseImage, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "修改失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (courseImage == null) {
            content = "为该课程选一个好看的封面吧~";
        } else {
            boolean isImage = true;
            try {
                if (!FileUtils.isImage(courseImage)) {
                    isImage = false;
                    content = "课程封面必须是图片类型哦~";
                }
            } catch (Exception e) {
                isImage = false;
                System.out.println("ERROR:用户在修改课程封面判断封面图片是否为图片类型时出错，错误原因：" + e);
                content = "系统在判断封面图片是否为图片类型时出错";
            }
            if (isImage) {
                if (courseDao.getCountById(courseId) == 0) {
                    content = "该课程不存在！";
                } else {
                    User user = (User)session.getAttribute("user");
                    Course course = courseDao.getCourseById(courseId);
                    if (course.getUserEmail().equals(user.getEmail())) {
                        String courseImageName = courseImage.getOriginalFilename();
                        String courseImageType = courseImageName.substring(courseImageName.lastIndexOf(".") + 1);//上传图片的后缀类型
                        if (!FileUtils.isImageByType(courseImageType)) {
                            content = "课程封面必须是图片类型哦~";
                        } else if (FileUtils.getFileSize(courseImage) > courseImageMaxSize) {
                            content = "图片太大啦，不要超过" + courseImageMaxSizeString + "哦~";
                        } else {
                            String oldCourseImage = course.getImage();
                            String oldCourseImageType = oldCourseImage.substring(oldCourseImage.lastIndexOf(".") + 1);
                            String idMd5 = Md5.md5(courseId);
                            String courseImageDir = courseResourceDir + idMd5 + "/";
                            String imageName = idMd5 + "." + courseImageType;
                            JSONObject uploadCourseImageResult = FileUtils.uploadFile(courseImage, imageName, courseImageDir);
                            if ("true".equals(uploadCourseImageResult.getString("status"))) {
                                Timestamp time = new Timestamp(System.currentTimeMillis());
                                course.setModifyTime(time);
                                if (courseImageType.equals(oldCourseImageType)) {
                                    status = "true";
                                    content = "修改成功！";
                                    try {
                                        if (courseDao.editCourse(course) == 0) {
                                            System.out.println("ERROR:修改课程" + course.toString() + "封面操作数据库失败，由于图片类型前后相同，故修改封面生效！");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("ERROR:修改课程" + course.toString() + "封面操作数据库失败，失败原因：" + e + "，由于图片类型前后相同，故修改封面生效！");
                                    }
                                } else {
                                    course.setImage(idMd5 + "/" + imageName);
                                    boolean insertSuccess = false;
                                    try {
                                        if (courseDao.editCourse(course) == 0) {
                                            System.out.println("ERROR:修改课程" + course.toString() + "封面操作数据库失败");
                                            content = "修改课程封面操作数据库失败！";
                                        } else {
                                            status = "true";
                                            content = "修改成功！";
                                            insertSuccess = true;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("ERROR:修改课程" + course.toString() + "封面操作数据库失败，失败原因：" + e);
                                        content = "修改课程封面操作数据库失败！";
                                    }
                                    if (!insertSuccess) {
                                        JSONObject deleteCourseImageResult = FileUtils.deleteFile(courseImageDir + imageName);
                                        System.out.println("INFO:修改课程" + course.toString() + "封面操作数据库失败后删除课程封面图片结果：" + deleteCourseImageResult.toString());
                                    }
                                }
                                if ("true".equals(status)) {
                                    String messageTitle = "您已成功修改课程：" + course.getName();
                                    String messageContent = createEditCourseImageSuccessEmailMessage(course);
                                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                                    System.out.println("INFO:课程" + course.toString() + "修改封面成功时创建消息通知结果：" + createMessageResult.toString());
                                }
                            } else {
                                System.out.println("ERROR:创建课程时上传封面图片失败，失败原因：" + uploadCourseImageResult.getString("content"));
                                content = "上传封面图片失败，失败原因" + uploadCourseImageResult.getString("content");
                            }
                        }
                    } else {
                        content = "只可以修改自己创建的课程！";
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createEditCourseImageSuccessEmailMessage(Course course) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>您于" + timeString + "修改课程:" + course.getName() + "&nbsp;&nbsp;封面成功，您可前往&nbsp;我的课程-我创建的&nbsp;进行查看</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能账号密码存在泄漏，请及时修改密码</p>";
        return message;
    }

    /**
     * 获取指定课程私有类型按照修改时间排序后的前N个课程
     *
     * @param isPrivate 课程私有类型
     * @param topNumber N
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseListByIsPrivateAndTopNumberOrderByModifyTime(String isPrivate, int topNumber) {
        if (courseDao.getCourseCountByIsPrivate(isPrivate) == 0) {
            return null;
        } else {
            List<Course> courseList = courseDao.getCourseByLimitAndIsPrivateOrderByModifyTime(0, topNumber, isPrivate);
            for (Course course : courseList) {
                if (userDao.getUserCountByEmail(course.getUserEmail()) != 0) {
                    User user = userDao.getUserByEmail(course.getUserEmail());
                    course.setUserName(user.getName());
                }
            }
            return courseList;
        }
    }

    /**
     * 获取指定课程私有类型按照学习人数排序后的前N个课程
     *
     * @param isPrivate 课程私有类型
     * @param topNumber N
     * @return 课程列表
     * @author 郭欣光
     */
    @Override
    public List<Course> getCourseListByIsPrivateAndTopNumberOrderByStudyNumber(String isPrivate, int topNumber) {
        if (courseDao.getCourseCountByIsPrivate(isPrivate) == 0) {
            return null;
        } else {
            List<Course> courseList = courseDao.getCourseByLimitAndIsPrivateOrderByStudyNumber(0, topNumber, isPrivate);
            for (Course course : courseList) {
                if (userDao.getUserCountByEmail(course.getUserEmail()) != 0) {
                    User user = userDao.getUserByEmail(course.getUserEmail());
                    course.setUserName(user.getName());
                }
            }
            return courseList;
        }
    }

    /**
     * 获取用户课程信息
     *
     * @param user       用户信息
     * @param coursePage 课程页数
     * @return 课程先关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getCourseByUser(User user, String coursePage) {
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
            int courseCount = courseDao.getCourseCountByUserEmail(user.getEmail());
            if (courseCount != 0) {
                coursePageNumber = ((courseCount % courseCountEachPage) == 0) ? (courseCount / courseCountEachPage) : (courseCount / courseCountEachPage + 1);
                if (coursePageInt <= coursePageNumber) {
                    status = "true";
                    hasCourse = "true";
                    courseList = courseDao.getCourseByUserEmailAndLimit(user.getEmail(), coursePageInt - 1, courseCountEachPage);
                    for (Course course : courseList) {
                        course.setUserName(user.getName());
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
     * 根据用户邮箱获取课程个数
     *
     * @param userEmail 用户邮箱
     * @return 课程个数
     */
    @Override
    public int getCourseCountByUserEmail(String userEmail) {
        return courseDao.getCourseCountByUserEmail(userEmail);
    }

    /**
     * 删除课程
     *
     * @param courseId 考试ID
     * @param request  用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteCourse(String courseId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else if (courseDao.getCountById(courseId) == 0) {
            content = "系统未检测到该课程信息！";
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseDao.getCourseById(courseId);
            if (user.getEmail().equals(course.getUserEmail())) {
                List<String> resourcePath = null;
                int lessonCount = lessonDao.getCountByCourseId(courseId);
                if (lessonCount != 0) {
                    List<Lesson> lessonList = lessonDao.getLessonByCourseIdAndLimitOrderByModifyTime(courseId, 0, lessonCount);
                    resourcePath = new ArrayList<String>();
                    for (Lesson lesson : lessonList) {
                        resourcePath.add(lesson.getContent());
                        int videoCount = videoDao.getCountByLessonId(lesson.getId());
                        if (videoCount != 0) {
                            List<Video> videoList = videoDao.getVideoByLessonId(lesson.getId());
                            for (Video video : videoList) {
                                resourcePath.add(video.getPath());
                            }
                        }
                        int lessonDataCount = lessonDataDao.getCountByLessonId(lesson.getId());
                        if (lessonDataCount != 0) {
                            List<LessonData> lessonDataList = lessonDataDao.getLessonDataByLessonId(lesson.getId());
                            for (LessonData lessonData : lessonDataList) {
                                resourcePath.add(lessonData.getPath());
                            }
                        }
                    }
                }
                try {
                    if (courseDao.deleteCourse(course) == 0) {
                        content = "操作数据库失败！";
                        System.out.println("ERROR:删除课程" + course.toString() + "时操作数据库失败");
                    } else {
                        status = "true";
                        content = "删除成功！";
                    }
                } catch (Exception e) {
                    content = "操作数据库失败！";
                    System.out.println("ERROR:删除课程" + course.toString() + "时操作数据库失败，失败原因：" + e);
                }
                if ("true".equals(status)) {
                    JSONObject deleteCourseImageResult = FileUtils.deleteFile(courseResourceDir + course.getImage());
                    System.out.println("INFO:删除课程" + course.toString() + "成功后删除封面图片结果" + deleteCourseImageResult);
                    if (resourcePath != null && resourcePath.size() > 0) {
                        for (String resource : resourcePath) {
                            JSONObject deleteResourceResult = FileUtils.deleteFile(courseResourceDir + resource);
                            System.out.println("INFO:删除课程" + course.toString() + "成功后删除资源：" + resource + "结果：" + deleteResourceResult.toString());
                        }
                    }
                    String messageTitle = "删除课时成功";
                    String messageContent = createDeleteCourseSuccessEmailMessage(course);
                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                    System.out.println("INFO:课程" + course.toString() + "删除成功时创建消息通知结果：" + createMessageResult.toString());
                }
            } else {
                content = "不可以删除不是自己创建的课程！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteCourseSuccessEmailMessage(Course course) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "删除课程：" + course.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}