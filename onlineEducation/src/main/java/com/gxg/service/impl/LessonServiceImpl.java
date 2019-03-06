package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.LessonDao;
import com.gxg.dao.LessonDataDao;
import com.gxg.dao.VideoDao;
import com.gxg.entities.*;
import com.gxg.service.LessonService;
import com.gxg.service.MessageService;
import com.gxg.utils.FileUtils;
import com.gxg.utils.Md5;
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
 * 课时相关业务处理
 * @author 郭欣光
 * @date 2019/2/19 10:28
 */

@Service(value = "lessonService")
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao;

    @Value("${lesson.count.each.page}")
    private int lessonCountEachPage;

    @Autowired
    private CourseDao courseDao;

    @Value("${course.resource.dir}")
    private String courseResourceDir;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LessonDataDao lessonDataDao;

    @Autowired
    private VideoDao videoDao;

    /**
     * 获取指定课程指定页数的课时信息
     *
     * @param courseId   课程ID
     * @param lessonPage 页数
     * @return 课时相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getLessonListByCourseId(String courseId, String lessonPage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int lessonPageNumber = 1;
        int lessonPageInt = 0;
        String hasLesson = "false";
        try {
            lessonPageInt = Integer.parseInt(lessonPage);
        } catch (Exception e) {
            lessonPageInt = 0;
        }
        List<Lesson> lessonList = null;
        if (lessonPageInt > 0) {
            int lessonCount = lessonDao.getCountByCourseId(courseId);
            if (lessonCount != 0) {
                lessonPageNumber = ((lessonCount % lessonCountEachPage) == 0) ? (lessonCount / lessonCountEachPage) : (lessonCount / lessonCountEachPage + 1);
                if (lessonPageInt <= lessonPageNumber) {
                    status = "true";
                    hasLesson = "true";
                    lessonList = lessonDao.getLessonByCourseIdAndLimitOrderByModifyTime(courseId, lessonPageInt - 1, lessonCountEachPage);
                }
            } else if (lessonPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("lessonPageNumber", lessonPageNumber);
        result.accumulate("lessonPage", lessonPageInt);
        result.accumulate("lessonList", lessonList);
        result.accumulate("hasLesson", hasLesson);
        return result;
    }

    /**
     * 获取指定课程ID的前N个课时信息
     *
     * @param courseId  课程ID
     * @param topNumber N
     * @return 课时信息
     * @author 郭欣光
     */
    @Override
    public List<Lesson> getLessonListByCourseIdAndTopNumber(String courseId, int topNumber) {
        if (lessonDao.getCountByCourseId(courseId) == 0) {
            return null;
        } else {
            List<Lesson> lessonList = lessonDao.getLessonByCourseIdAndLimitOrderByModifyTime(courseId, 0, topNumber);
            return lessonList;
        }
    }

    /**
     * 创建课时
     *
     * @param courseId      课程ID
     * @param lessonName    课时名称
     * @param lessonContent 课时内容
     * @param request       用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createLesson(String courseId, String lessonName, String lessonContent, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "创建失败";
        HttpSession session = request.getSession();
        if (StringUtils.isEmpty(lessonName)) {
            content = "给课时起个名字吧~";
        } else if (lessonName.length() > 100) {
            content = "课时名称不能超过100字符~";
        } else if (StringUtils.isEmpty(lessonContent)) {
            content = "写点内容呗~";
        } else if (courseDao.getCountById(courseId) == 0) {
            content = "没有该课程！";
        } else if(session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else {
            User user = (User)session.getAttribute("user");
            Course course = courseDao.getCourseById(courseId);
            if (course.getUserEmail().equals(user.getEmail())) {
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String timeString = time.toString();
                String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                while (lessonDao.getCountById(id) != 0) {
                    long idLong = Long.parseLong(id);
                    Random random = new Random();
                    idLong += random.nextInt(100);
                    id = idLong + "";
                    if (id.length() > 17) {
                        id = id.substring(0, 17);
                    }
                }
                String filePath = courseResourceDir + Md5.md5(courseId) + "/lesson/";
                JSONObject writeFileResult = FileUtils.writeFile(filePath, Md5.md5(id) + ".html", lessonContent);
                if ("true".equals(writeFileResult.getString("status"))) {
                    boolean insertSuccess = false;
                    Lesson lesson = new Lesson();
                    lesson.setId(id);
                    lesson.setName(lessonName);
                    lesson.setContent(Md5.md5(courseId) + "/lesson/" + Md5.md5(id) + ".html");
                    lesson.setCreateTime(time);
                    lesson.setModifyTime(time);
                    lesson.setCourseId(courseId);
                    try {
                        if (lessonDao.createLesson(lesson) == 0) {
                            System.out.println("ERROR:创建课时时操作数据库失败");
                            content = "创建课时时操作数据库失败！";
                        } else {
                            status = "true";
                            insertSuccess = true;
                            content = id;
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR:创建课时时操作数据库失败，失败原因：" + e);
                        content = "创建课时时操作数据库失败！";
                    }
                    if (insertSuccess) {
                        course.setModifyTime(time);
                        try {
                            if (courseDao.editCourse(course) == 0) {
                                System.out.println("ERROR:创建课时成功后更新课程信息" + course.toString() + "失败！");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:创建课时成功后更新课程信息" + course.toString() + "失败，失败原因：" + e);
                        }
                        String messageTitle = "您已成功创建课时：" + lessonName;
                        String messageContent = createCreateLessonSuccessEmailMessage(lesson);
                        JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                        System.out.println("INFO:课时" + lesson.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                    } else {
                        JSONObject deleteFileResult = FileUtils.deleteFile(filePath + Md5.md5(id) + ".html");
                        System.out.println("INFO:创建课时操作数据库失败后删除文件结果:" + deleteFileResult);
                    }
                } else {
                    content = "系统写入文件失败，失败原因：" + writeFileResult.getString("content");
                    System.out.println("ERROR:系统创建课时时写入文件失败，失败原因：" + writeFileResult.getString("content"));
                }
            } else {
                content = "您没有给他人课程创建课时的权限！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateLessonSuccessEmailMessage(Lesson lesson) {
        String timeString = lesson.getCreateTime().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "创建课时：" + lesson.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    /**
     * 根据课时ID获取课时信息
     *
     * @param lessonId 课时ID
     * @return 课时信息
     * @author 郭欣光
     */
    @Override
    public Lesson getLessonById(String lessonId) {
        if (lessonDao.getCountById(lessonId) == 0) {
            return null;
        } else {
            Lesson lesson = lessonDao.getLessonById(lessonId);
            String lessonFile = courseResourceDir + lesson.getContent();
            JSONObject fileContent = FileUtils.readFile(lessonFile);
            lesson.setContent(fileContent.getString("content"));
            return lesson;
        }
    }


    /**
     * 修改课时内容
     *
     * @param lessonId      课时ID
     * @param lessonName    课时名称
     * @param lessonContent 课时内容
     * @param request       用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String editLesson(String lessonId, String lessonName, String lessonContent, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "修改失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else if (StringUtils.isEmpty(lessonName)) {
            content = "给课时起个名字吧~";
        } else if (lessonName.length() > 100) {
            content = "课时名称不能超过100字符~";
        } else if (StringUtils.isEmpty(lessonContent)) {
            content = "写点内容呗~";
        } else if (lessonDao.getCountById(lessonId) == 0) {
            content = "没有该课时！";
        } else {
            User user = (User)session.getAttribute("user");
            Lesson lesson = lessonDao.getLessonById(lessonId);
            if (courseDao.getCountById(lesson.getCourseId()) == 0) {
                content = "找不到课时相关的课程！";
            } else {
                Course course = courseDao.getCourseById(lesson.getCourseId());
                if (course.getUserEmail() != null && course.getUserEmail().equals(user.getEmail())) {
                    String filePath = courseResourceDir + Md5.md5(lesson.getCourseId()) + "/lesson/";
                    JSONObject writeFileResult = FileUtils.writeFile(filePath, Md5.md5(lessonId) + ".html", lessonContent);
                    if ("true".equals(writeFileResult.getString("status"))) {
                        boolean updateSuccess = false;
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        lesson.setName(lessonName);
                        lesson.setModifyTime(time);
                        try {
                            if (lessonDao.editLesson(lesson) == 0) {
                                content = "课时内容修改成功，其他信息由于操作数据库失败导致修改失败！";
                                System.out.println("ERROR:系统修改课时" + lesson.toString() + "操作数据库失败");
                            } else {
                                status = "true";
                                content = lessonId;
                                updateSuccess = true;
                            }
                        } catch (Exception e) {
                            content = "课时内容修改成功，其他信息由于操作数据库失败导致修改失败！";
                            System.out.println("ERROR:系统修改课时" + lesson.toString() + "操作数据库失败，失败原因：" + e);
                        }
                        if (updateSuccess) {
                            course.setModifyTime(time);
                            try {
                                if (courseDao.editCourse(course) == 0) {
                                    System.out.println("ERROR:系统修改课时" + lesson.toString() + "所属课程" + course.toString() + "操作数据库失败");
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:系统修改课时" + lesson.toString() + "所属课程" + course.toString() + "操作数据库失败，失败原因：" + e);
                            }
                            String messageTitle = "您已成功修改课时：" + lessonName;
                            String messageContent = createEidtLessonSuccessEmailMessage(lesson);
                            JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                            System.out.println("INFO:课时" + lesson.toString() + "修改成功时创建消息通知结果：" + createMessageResult.toString());
                        }
                    } else {
                        content = "系统写入文件失败，失败原因：" + writeFileResult.getString("content");
                        System.out.println("ERROR:系统修改课时" + lesson.toString() + "时写入文件失败，失败原因：" + writeFileResult.getString("content"));
                    }
                } else {
                    content = "您没有修改他人课时的权限！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createEidtLessonSuccessEmailMessage(Lesson lesson) {
        String timeString = lesson.getCreateTime().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "修改课时：" + lesson.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    /**
     * 删除课时
     *
     * @param lessonId 课时ID
     * @param request  用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteLesson(String lessonId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else if (lessonDao.getCountById(lessonId) == 0) {
            content = "系统未检测到该课时信息！";
        } else {
            User user = (User)session.getAttribute("user");
            Lesson lesson = lessonDao.getLessonById(lessonId);
            if (courseDao.getCountById(lesson.getCourseId()) == 0) {
                content = "系统未检测到相关课程信息！";
            } else {
                Course course = courseDao.getCourseById(lesson.getCourseId());
                if (user.getEmail().equals(course.getUserEmail())) {
                    List<LessonData> lessonDataList = null;
                    List<Video> videoList = null;
                    if (lessonDataDao.getCountByLessonId(lessonId) != 0) {
                        lessonDataList = lessonDataDao.getLessonDataByLessonId(lessonId);
                    }
                    if (videoDao.getCountByLessonId(lessonId) == 0) {
                        videoList = videoDao.getVideoByLessonId(lessonId);
                    }
                    try {
                        if (lessonDao.deleteLesson(lesson) == 0) {
                            System.out.println("ERROR:删除课时信息" + lesson.toString() + "操作数据库失败");
                            content = "删除课时信息时操作数据库失败！";
                        } else {
                            status = "true";
                            content = "删除成功！";
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR:删除课时信息" + lesson.toString() + "操作数据库失败，失败原因：" + e);
                        content = "删除课时信息时操作数据库失败！";
                    }
                    if ("true".equals(status)) {
                        JSONObject deleteFileResult = FileUtils.deleteFile(courseResourceDir + lesson.getContent());
                        System.out.println("INFO:删除课时" + lesson.toString() + "成功后，删除文件结果：" + deleteFileResult.toString());
                        if (lessonDataList != null) {
                            for (LessonData lessonData : lessonDataList) {
                                try {
                                    if (lessonDataDao.deleteLessonData(lessonData) == 0) {
                                        System.out.println("ERROR:删除课时" + lesson.toString() + "成功后，删除课时资料" + lessonData.toString() + "操作数据库失败");
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:删除课时" + lesson.toString() + "成功后，删除课时资料" + lessonData.toString() + "操作数据库失败，失败原因：" + e);
                                }
                                JSONObject deleteLessonDataFileResult = FileUtils.deleteFile(courseResourceDir + lessonData.getPath());
                                System.out.println("INFO:删除课时" + lesson.toString() + "成功后，删除课时资料" + lessonData.toString() + "文件结果：" + deleteLessonDataFileResult.toString());
                            }
                        }
                        if (videoList != null) {
                            for (Video video : videoList) {
                                try {
                                    if (videoDao.deleteVideo(video) == 0) {
                                        System.out.println("ERROR:删除课时" + lesson.toString() + "成功后，删除视频" + video.toString() + "操作数据库失败");
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:删除课时" + lesson.toString() + "成功后，删除视频" + video.toString() + "操作数据库失败，失败原因：" + e);
                                }
                                JSONObject deleteVideoFileResult = FileUtils.deleteFile(courseResourceDir + video.getPath());
                                System.out.println("INFO:删除课时" + lesson.toString() + "成功后，删除视频" + video.toString() + "文件结果：" + deleteVideoFileResult.toString());
                            }
                        }
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        course.setModifyTime(time);
                        try {
                            if (courseDao.editCourse(course) == 0) {
                                System.out.println("ERROR:删除课时" + lesson.toString() + "成功后更新课程" + course.toString() + "失败");
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:删除课时" + lesson.toString() + "成功后更新课程" + course.toString() + "失败，失败原因：" + e);
                        }
                        String messageTitle = "删除课时成功";
                        String messageContent = createDeleteLessonSuccessEmailMessage(lesson);
                        JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                        System.out.println("INFO:课时" + lesson.toString() + "删除成功时创建消息通知结果：" + createMessageResult.toString());
                    }
                } else {
                    content = "您没有删除他人课时的权限！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteLessonSuccessEmailMessage(Lesson lesson) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString();
        String message = "<p>恭喜您，您于" + timeString + "删除课时：" + lesson.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}
