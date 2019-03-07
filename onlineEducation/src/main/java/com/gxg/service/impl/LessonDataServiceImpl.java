package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.LessonDao;
import com.gxg.dao.LessonDataDao;
import com.gxg.entities.Course;
import com.gxg.entities.Lesson;
import com.gxg.entities.LessonData;
import com.gxg.entities.User;
import com.gxg.service.LessonDataService;
import com.gxg.service.MessageService;
import com.gxg.utils.FileUtils;
import com.gxg.utils.Md5;
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
 * @author 郭欣光
 * @date 2019/2/28 10:54
 */

@Service(value = "lessonDataService")
public class LessonDataServiceImpl implements LessonDataService {

    @Autowired
    private LessonDataDao lessonDataDao;

    @Autowired
    private LessonDao lessonDao;

    @Value("${data.max.size}")
    private long dataMaxSize;

    @Value("${data.max.size.strintg}")
    private String dataMaxSizeString;

    @Autowired
    private CourseDao courseDao;

    @Value("${course.resource.dir}")
    private String courseResourceDir;

    @Autowired
    private MessageService messageService;

    /**
     * 获取某一课时的资料数量
     *
     * @param lessonId 课时ID
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String getLessonDataNumberByLessonId(String lessonId) {
        JSONObject result = new JSONObject();
        String status = "true";
        int lessonDataCount = lessonDataDao.getCountByLessonId(lessonId);
        String content = lessonDataCount + "";
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 创建课时资料
     *
     * @param lessonId       课时ID
     * @param lessonDataFile 课时资料
     * @param request        用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createLessonDate(String lessonId, MultipartFile lessonDataFile, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "上传资料失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (lessonDao.getCountById(lessonId) == 0) {
            content = "系统未检测到相关课时信息！";
        } else if (lessonDataFile == null) {
            content = "请选择上传的资料！";
        } else if (FileUtils.getFileSize(lessonDataFile) > dataMaxSize) {
            content = "资料大小不能超过" + dataMaxSizeString;
        } else {
            Lesson lesson = lessonDao.getLessonById(lessonId);
            if (courseDao.getCountById(lesson.getCourseId()) == 0) {
                content = "系统未检测到相关课程信息！";
            } else {
                Course course = courseDao.getCourseById(lesson.getCourseId());
                User user = (User)session.getAttribute("user");
                if (course.getUserEmail().equals(user.getEmail())) {
                    String name = lessonDataFile.getOriginalFilename();
                    String fileType = name.substring(name.lastIndexOf(".") + 1);//上传文件的后缀类型
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (lessonDataDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    String idMd5 = Md5.md5(id);
                    String filePath = courseResourceDir + Md5.md5(lesson.getCourseId()) + "/lesson/data/" + Md5.md5(lessonId) + "/";
                    String fileName = idMd5 + "." + fileType;
                    JSONObject uploadFileResult = FileUtils.uploadFile(lessonDataFile, fileName, filePath);
                    if ("true".equals(uploadFileResult.getString("status"))) {
                        if (name.length() > 200) {
                            name = name.substring(0, 200);
                        }
                        LessonData lessonData = new LessonData();
                        lessonData.setId(id);
                        lessonData.setName(name);
                        lessonData.setPath(Md5.md5(lesson.getCourseId()) + "/lesson/data/" + Md5.md5(lessonId) + "/" + fileName);
                        lessonData.setCreateTime(time);
                        lessonData.setLessonId(lessonId);
                        try {
                            if (lessonDataDao.createLessonData(lessonData) == 0) {
                                System.out.println("ERROR:创建课时资料" + lessonData.toString() + "操作数据库失败！");
                                content = "操作数据库失败！";
                            } else {
                                status = "true";
                                content = "创建成功";
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:创建课时资料" + lessonData.toString() + "操作数据库失败！失败原因：" + e);
                            content = "操作数据库失败！";
                        }
                        if ("true".equals(status)) {
                            lesson.setModifyTime(time);
                            course.setModifyTime(time);
                            try {
                                if (lessonDao.editLesson(lesson) == 0) {
                                    System.out.println("ERROR:创建课时资料" + lessonData.toString() + "成功后修改课时" + lesson.toString() + "失败！");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:创建课时资料" + lessonData.toString() + "成功后修改课时" + lesson.toString() + "失败！失败原因：" + e);
                            }
                            try {
                                if (courseDao.editCourse(course) == 0) {
                                    System.out.println("ERROR:创建课时资料" + lessonData.toString() + "成功后修改课程" + course.toString() + "失败！");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:创建课时资料" + lessonData.toString() + "成功后修改课程" + course.toString() + "失败，失败原因：" + e);
                            }
                            String messageTitle = "上传课时资料成功";
                            String messageContent = createCreateLessonDataSuccessEmailMessage(lessonData);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:课时资料" + lessonData.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                        } else {
                            JSONObject deleteFileResult = FileUtils.deleteFile(filePath + fileName);
                            System.out.println("INFO:创建课时资料" + lessonData.toString() + "操作数据库失败后删除文件结果：" + deleteFileResult.toString());
                        }
                    } else {
                        System.out.println("ERROR:创建课时资料时上传文件失败！失败原因：" + uploadFileResult.getString("content"));
                        content = "上传文件失败！失败原因：" + uploadFileResult.getString("content");
                    }
                } else {
                    content = "您没有权限对不属于自己的课时上传资料！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateLessonDataSuccessEmailMessage(LessonData lessonData) {
        String timeString = lessonData.getCreateTime().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "上传课时资料：" + lessonData.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    /**
     * 根据课时ID获取课时资料
     *
     * @param lessonId 课时ID
     * @return 课时资料相关
     * @author 郭欣光
     */
    @Override
    public String getLessonDataListByLessonId(String lessonId) {
        JSONObject result = new JSONObject();
        String status = "true";
        List<LessonData> content = null;
        if (lessonDataDao.getCountByLessonId(lessonId) != 0) {
            content = lessonDataDao.getLessonDataByLessonId(lessonId);
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 删除课时资料
     *
     * @param lessonDataId 课时资料ID
     * @param request      用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteLessonData(String lessonDataId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (lessonDataDao.getCountById(lessonDataId) == 0) {
            content = "系统未找到课时资料信息";
        } else {
            User user = (User)session.getAttribute("user");
            LessonData lessonData = lessonDataDao.getLessonDataById(lessonDataId);
            if (lessonDao.getCountById(lessonData.getLessonId()) == 0) {
                content = "系统未找到课时信息";
            } else {
                Lesson lesson = lessonDao.getLessonById(lessonData.getLessonId());
                if (courseDao.getCountById(lesson.getCourseId()) == 0) {
                    content = "系统未找到课程信息";
                } else {
                    Course course = courseDao.getCourseById(lesson.getCourseId());
                    if (user.getEmail().equals(course.getUserEmail())) {
                        try {
                            if (lessonDataDao.deleteLessonData(lessonData) == 0) {
                                System.out.println("ERROR:删除课时资料" + lessonData.toString() + "操作数据库失败");
                                content = "删除课时资料操作数据库失败！";
                            } else {
                                status = "true";
                                content = "删除成功！";
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:删除课时资料" + lessonData.toString() + "操作数据库失败，失败原因：" + e);
                            content = "删除课时资料操作数据库失败！";
                        }
                        if ("true".equals(status)) {
                            JSONObject deleteFileResult = FileUtils.deleteFile(courseResourceDir + lessonData.getPath());
                            System.out.println("INFO:删除课时资料" + lessonData.toString() + "成功后，删除文件结果：" + deleteFileResult.toString());
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            lesson.setModifyTime(time);
                            course.setModifyTime(time);
                            try {
                                if (lessonDao.editLesson(lesson) == 0) {
                                    System.out.println("ERROR:删除课时资料" + lessonData.toString() + "成功后更新课时失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:删除课时资料" + lessonData.toString() + "成功后更新课时失败，失败原因：" + e);
                            }
                            try {
                                if (courseDao.editCourse(course) == 0) {
                                    System.out.println("ERROR:删除课时资料" + lessonData.toString() + "成功后更新课程" + course.toString() + "失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:删除课时资料" + lessonData.toString() + "成功后更新课程" + course.toString() + "失败，失败原因：" + e);
                            }
                            String messageTitle = "删除课时资料成功";
                            String messageContent = createDeleteLessonDataSuccessEmailMessage(lessonData);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:课时资料" + lessonData.toString() + "删除成功时创建消息通知结果：" + createMessageResult.toString());
                        }
                    } else {
                        content = "抱歉，您没有权限删除他人的课时资料";
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteLessonDataSuccessEmailMessage(LessonData lessonData) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "删除课时资料：" + lessonData.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}
