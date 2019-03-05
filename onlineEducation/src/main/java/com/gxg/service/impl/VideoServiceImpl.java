package com.gxg.service.impl;

import com.gxg.dao.CourseDao;
import com.gxg.dao.LessonDao;
import com.gxg.dao.VideoDao;
import com.gxg.entities.Course;
import com.gxg.entities.Lesson;
import com.gxg.entities.User;
import com.gxg.entities.Video;
import com.gxg.service.MessageService;
import com.gxg.service.VideoService;
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
import java.util.Random;

/**
 * 课时视频相关业务处理
 * @author 郭欣光
 * @date 2019/3/5 11:01
 */
@Service(value = "videoService")
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private LessonDao lessonDao;

    @Value("${video.max.size}")
    private long videoMaxSize;

    @Value("${video.max.size.strintg}")
    private String videoMaxSizeString;

    @Autowired
    private CourseDao courseDao;

    @Value("${course.resource.dir}")
    private String courseResourceDir;

    @Autowired
    private MessageService messageService;

    /**
     * 根据课时ID获取视频个数
     *
     * @param lessonId 课时ID
     * @return 视频个数先关信息
     * @author 郭欣光
     */
    @Override
    public String getVideoNumberByLessonId(String lessonId) {
        JSONObject result = new JSONObject();
        String status = "true";
        int videoCount = videoDao.getCountByLessonId(lessonId);
        String content = videoCount + "";
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 创建课时视频
     *
     * @param lessonId  课时ID
     * @param videoName 视频名称
     * @param videoFile 视频文件
     * @param request   用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createVideo(String lessonId, String videoName, MultipartFile videoFile, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "上传失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录信息，请刷新页面后重试！";
        } else if (lessonDao.getCountById(lessonId) == 0) {
            content = "系统未检测到相关课时信息！";
        } else if (StringUtils.isEmpty(videoName)) {
            content = "视频名称不能为空！";
        } else if (videoName.length() > 100) {
            content = "视频名称长度不能超过100字节！";
        } else if (videoFile.getSize() > videoMaxSize) {
            content = "视频大小不能超过" + videoMaxSizeString;
        } else {
            boolean isVideo = false;
            try {
                if (FileUtils.isMp4H264OrOgg(videoFile)) {
                    isVideo = true;
                } else {
                    content = "仅支持mp4(avc(h264))以及Ogg格式！";
                }
            } catch (Exception e) {
                System.out.println("ERROR:系统在上传视频判断视频类型时出错，错误信息：" + e);
                content = "系统在判断文件类型时出错！";
            }
            if (isVideo) {
                String videoFileName = videoFile.getOriginalFilename();
                String videoFileType = videoFileName.substring(videoFileName.lastIndexOf(".") + 1);//上传文件的后缀类型
                if (FileUtils.isMp4H264OrOggByFileType(videoFileType)) {
                    Lesson lesson = lessonDao.getLessonById(lessonId);
                    if (courseDao.getCountById(lesson.getCourseId()) == 0) {
                        content = "系统未检测到相关课程信息！";
                    } else {
                        Course course = courseDao.getCourseById(lesson.getCourseId());
                        User user = (User)session.getAttribute("user");
                        if (course.getUserEmail().equals(user.getEmail())) {
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String timeString = time.toString();
                            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                            while (videoDao.getCountById(id) != 0) {
                                long idLong = Long.parseLong(id);
                                Random random = new Random();
                                idLong += random.nextInt(100);
                                id = idLong + "";
                                if (id.length() > 17) {
                                    id = id.substring(0, 17);
                                }
                            }
                            String idMd5 = Md5.md5(id);
                            String filePath = courseResourceDir + Md5.md5(lesson.getCourseId()) + "/lesson/video/" + Md5.md5(lessonId) + "/";
                            String fileName = idMd5 + "." + videoFileType;
                            JSONObject uploadFileResult = FileUtils.uploadFile(videoFile, fileName, filePath);
                            if ("true".equals(uploadFileResult.getString("status"))) {
                                Video video = new Video();
                                video.setId(id);
                                video.setName(videoName);
                                video.setPath(Md5.md5(lesson.getCourseId()) + "/lesson/video/" + Md5.md5(lessonId) + "/" + fileName);
                                video.setCreateTime(time);
                                video.setLessonId(lessonId);
                                boolean insertSuccess = false;
                                try {
                                    if (videoDao.createVideo(video) == 0) {
                                        System.out.println("ERROR:系统在上传视频" + video.toString() + "时操作数据库失败");
                                        content = "操作数据库失败！";
                                    } else {
                                        insertSuccess = true;
                                        status = "true";
                                        content = "上传成功！";
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:系统在上传视频" + video.toString() + "时操作数据库失败，失败原因：" + e);
                                    content = "操作数据库失败！";
                                }
                                if (insertSuccess) {
                                    lesson.setModifyTime(time);
                                    course.setModifyTime(time);
                                    try {
                                        if (lessonDao.editLesson(lesson) == 0) {
                                            System.out.println("ERROR:创建课时视频" + video.toString() + "成功后修改课时" + lesson.toString() + "失败！");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("ERROR:创建课时视频" + video.toString() + "成功后修改课时" + lesson.toString() + "失败！失败原因：" + e);
                                    }
                                    try {
                                        if (courseDao.editCourse(course) == 0) {
                                            System.out.println("ERROR:创建课时视频" + video.toString() + "成功后修改课程" + course.toString() + "失败！");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("ERROR:创建课时视频" + video.toString() + "成功后修改课程" + course.toString() + "失败，失败原因：" + e);
                                    }
                                    String messageTitle = "上传视频成功";
                                    String messageContent = createCreateVideoSuccessEmailMessage(video);
                                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                                    System.out.println("INFO:视频" + video.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                                } else {
                                    JSONObject deleteFileResult = FileUtils.deleteFile(filePath + fileName);
                                    System.out.println("INFO:创建课时视频" + video.toString() + "操作数据库失败后删除文件结果：" + deleteFileResult.toString());
                                }
                            } else {
                                System.out.println("ERROR:系统在上传视频时上传文件失败，失败原因：" + uploadFileResult.getString("content"));
                                content = "上传失败，失败原因：" + uploadFileResult.getString("content");
                            }
                        } else {
                            content = "您没有权限对不属于自己的课时上传视频！";
                        }
                    }
                } else {
                    content = "仅支持mp4(avc(h264))以及Ogg格式！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateVideoSuccessEmailMessage(Video video) {
        String timeString = video.getCreateTime().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "上传视频：" + video.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}
