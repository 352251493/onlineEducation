package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Course;
import com.gxg.entities.Message;
import com.gxg.entities.User;
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
                                content = "创建成功！";
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
}