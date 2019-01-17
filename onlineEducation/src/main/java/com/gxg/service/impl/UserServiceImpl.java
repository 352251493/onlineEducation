package com.gxg.service.impl;

import com.gxg.dao.UserDao;
import com.gxg.entities.User;
import com.gxg.service.MailService;
import com.gxg.service.UserService;
import com.gxg.utils.FileUtils;
import com.gxg.utils.Md5;
import com.gxg.utils.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关业务处理
 * @author 郭欣光
 * @date 2019/1/16 14:48
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Value("${user.headImage.max.size}")
    private long userHeadImageMaxSize;

    @Value("${user.headImage.max.size.string}")
    private String userHeadImageMaxSizeString;

    @Autowired
    private UserDao userDao;

    @Value("${user.resource.dir}")
    private String userResourceDir;

    @Value("${sys.name}")
    private String sysName;

    @Autowired
    Configuration configuration;//这里注入的是freeMarker的configuration

    @Autowired
    private MailService mailService;

    @Value("${sys.root.url}")
    private String sysRootUrl;

    /**
     * 用户注册信息处理
     *
     * @param email      邮箱
     * @param name       姓名
     * @param password   密码
     * @param repassword 重复密码
     * @param sex        性别
     * @param job        职业
     * @param headImage  头像
     * @param request    用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String register(String email, String name, String password, String repassword, String sex, String job, MultipartFile headImage, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "注册失败！";
        if (StringUtils.isEmpty(email)) {
            content = "邮箱不能为空，请输入您的邮箱";
        } else if (email.length() > 150) {
            content = "邮箱长度不能超过150个字符！";
        } else if (!StringUtils.isEmail(email)) {
            content = "给个正确的邮箱格式吧~";
        } else if (StringUtils.isEmpty(name)) {
            content = "留下您的姓名吧！";
        } else if (name.length() > 100) {
            content = "您的姓名太长啦，超过100字符我有点记不住";
        } else if (StringUtils.isEmpty(password)) {
            content = "留下密码才安全";
        } else if (password.length() > 20) {
            content = "密码20字符以内就很安全啦！";
        } else if (!password.equals(repassword)) {
            content = "两次输入的密码不一致，我该记住哪一个呢？";
        } else if (StringUtils.isEmpty(sex)) {
            content = "咦？难道没有默认性别吗？";
        } else if (sex.length() > 1) {
            content = "难道性别长度被篡改了？";
        } else if (StringUtils.isEmpty(job)) {
            content = "咦？难道没有默认职业吗？";
        } else if (job.length() > 10) {
            content = "难道职位长度被篡改了？";
        } else if (headImage == null) {
            content = "选一个头像更有吸引力哦~";
        } else {
            boolean isImage = true;
            try {
                if (!FileUtils.isImage(headImage)) {
                    isImage = false;
                    content = "上传的头像必须是图片类型哦~";
                }
            } catch (Exception e) {
                isImage = false;
                System.out.println("ERROR:用户注册在判断上传头像是否为图片时出错，错误原因：" + e);
                content = "系统在判断上传头像是否为图片类型时出错";
            }
            if (isImage) {
                String headImageName = headImage.getOriginalFilename();
                String headImageType = headImageName.substring(headImageName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtils.isImageByType(headImageType)) {
                    content = "上传的头像必须是图片类型哦~";
                } else if (FileUtils.getFileSize(headImage) > userHeadImageMaxSize) {
                    content = "头像太大啦，不要超过" + userHeadImageMaxSizeString + "哦~";
                } else if (userDao.getUserCountByEmail(email) > 0) {
                    content = "该邮箱已被注册！";
                } else {
                    String emailMd5 = Md5.md5(email);
                    String headImageDir = userResourceDir + emailMd5 + "/";
                    String imageName = emailMd5 + "." + headImageType;
                    JSONObject uploadHeadImageResult = FileUtils.uploadFile(headImage, imageName, headImageDir);
                    if ("true".equals(uploadHeadImageResult.getString("status"))) {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(Md5.md5(password));
                        user.setName(name);
                        user.setSex(sex);
                        user.setRole(job);
                        user.setHeadImage(emailMd5 + "/" + imageName);
                        user.setCreateTime(time);
                        user.setIsVerification("0");
                        boolean insertSuccess = false;
                        try {
                            if (userDao.createUser(user) == 0) {
                                System.out.println("ERROR:用户注册时操作数据库失败！");
                                content = "操作数据库失败！";
                            } else {
                                insertSuccess = true;
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR:用户注册时操作数据库失败！失败原因：" + e);
                            content = "操作数据库失败！";
                        }
                        if (insertSuccess) {
                            boolean isSendEmailSuccess = false;
                            String emailMessage = createUserRegisterEmailMessage(user);
                            Map<String, Object> model = new HashMap<String, Object>();
                            model.put("time", new Date());
                            model.put("emailMessage", emailMessage);
                            model.put("toUserName", name);
                            model.put("fromUserName", sysName);
                            try {
                                Template t = configuration.getTemplate("email_html_temp.ftl");
                                String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                                JSONObject sendEmailResult = mailService.sendHtmlMail(email, "邮箱验证", emailContent);
                                if ("true".equals(sendEmailResult.getString("status"))) {
                                    isSendEmailSuccess = true;
                                } else {
                                    System.out.println("ERROR:用户注册时发送邮件失败！");
                                    content = "系统向邮箱中发送验证信息失败，请确认您的邮箱信息是否正确，如邮箱信息正确，请您稍后再次尝试！";
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR:用户注册时发送邮件失败，失败原因：" + e);
                                content = "系统向邮箱中发送验证信息失败，请确认您的邮箱信息是否正确，如邮箱信息正确，请您稍后再次尝试！";
                            }
                            if (isSendEmailSuccess) {
                                status = "true";
                                content = "为了您的信息安全，验证信息已发送至您的邮箱，请您在两小时之内点击邮箱内链接完成验证";
                            } else {
                                boolean deleteUserSuccess = false;
                                try {
                                    if (userDao.deleteUserByEmail(email) == 0) {
                                        System.out.println("ERROR:用户注册时发送邮件失败，删除用户信息时操作数据库出错！");
                                    } else {
                                        deleteUserSuccess = true;
                                    }
                                } catch (Exception e) {
                                    System.out.println("ERROR:用户注册时发送邮件失败，删除用户信息时操作数据库出错！错误信息为：" + e);
                                }
                                if (deleteUserSuccess) {
                                    JSONObject deleteHeadImageResult = FileUtils.deleteFile(headImageDir + imageName);
                                    System.out.println("INFO:用户注册时发送邮件失败后删除头像结果：" + deleteHeadImageResult.toString());
                                }
                            }
                        } else {
                            JSONObject deleteHeadImageResult = FileUtils.deleteFile(headImageDir + imageName);
                            System.out.println("INFO:用户注册时操作数据库失败后删除头像结果：" + deleteHeadImageResult.toString());
                        }
                    } else {
                        System.out.println("ERROR:用户注册时上传头像失败，失败原因：" + uploadHeadImageResult.getString("content"));
                        content = "上传头像失败，失败原因：" + uploadHeadImageResult.getString("content");
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createUserRegisterEmailMessage(User user) {
        String message;
        if ("教师".equals(user.getRole())) {
            message = "<p>欢迎您注册" + sysName + "，我们将竭诚为您服务，您可以随意发布课程及其内容（注意遵守国家相关法律法规），您的公开课程将面向所有用户，您的付出将为祖国培养更多的人才，我们再次对您表示感谢。" +
                    "如果您有部分课程不愿意公开，可以发布为私有课程，您可以手动添加学生前来学习，方便您正常的教学。" +
                    "为了您的信息安全，请您点击下列连接完成邮箱验证：<a href='" + sysRootUrl + "user/register/" + user.getEmail() + "/" + registerRule(user) + "/'>" + sysRootUrl + "user/register/" + user.getEmail() + "/" + registerRule(user) + "/ </a></p>";
            message += "<p style='color:red'>请在两小时内完成验证，否则将导致注册失败！</p>";
            message += "祝您身体健康，工作愉快！";
        } else {
            message = "<p>欢迎您注册" + sysName + "，我们将竭诚为您服务，" + sysName + "有大量课程资源，您可以免费学习所有公开课程，私有课程只有教师主动添加才可以进行学习。" +
                    "我们相信在这里您的学习成绩将有一个质的的飞跃！" +
                    "为了您的信息安全，请您点击下列连接完成邮箱验证：<a href='" + sysRootUrl + "user/register/" + user.getEmail() + "/" + registerRule(user) + "/'>" + sysRootUrl + "user/register/" + user.getEmail() + "/" + registerRule(user) + "/ </a></p>";
            message += "<p style='color:red'>请在两小时内完成验证，否则将导致注册失败！</p>";
            message += "祝您身体健康，学业有成！";
        }
        return message;
    }

    /**
     * 注册验证规则
     * @param user 用户信息
     * @return 密文
     */
    private String registerRule(User user) {
        String message = Md5.md5(user.getEmail() + user.getPassword());
        return message;
    }

    /**
     * 删除用户
     *
     * @param user 用户信息
     * @return 删除结果
     */
    @Override
    public JSONObject deleteUser(User user) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        if (userDao.getUserCountByEmail(user.getEmail()) == 0) {
            content = "该用户不存在！";
        } else {
            boolean isDeleteUser = false;
            try {
                if (userDao.deleteUserByEmail(user.getEmail()) == 0) {
                    System.out.println("ERROR:删除用户" + user.getEmail() + "失败！");
                    content = "删除失败！";
                } else {
                    isDeleteUser = true;
                    status = "true";
                    content= "删除成功！";
                }
            } catch (Exception e) {
                System.out.println("ERROR:删除用户" + user.getEmail() + "失败，失败原因：" + e);
                content = "删除失败，失败原因：" + e;
            }
            if (isDeleteUser) {
                String headImage = userResourceDir + user.getHeadImage();
                JSONObject deleteHeadImageResult = FileUtils.deleteFile(headImage);
                System.out.println("INFO:删除用户时删除头像结果：" + deleteHeadImageResult.toString());
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }

    /**
     * 用户注册验证
     *
     * @param email            邮箱
     * @param verificationRule 验证规则
     * @return 验证结果
     */
    @Override
    public JSONObject register(String email, String verificationRule) {
        JSONObject result = new JSONObject();
        String promptTitle = "验证失败";
        String promptMessage = "对不起，邮箱验证失败，请您确认验证时间是否超时或向您邮箱发送的验证连接的正确性";
        if (userDao.getUserCountByEmail(email) == 0) {
            promptMessage = "对不起，没有查询到" + email + "相关的注册信息，请您确认验证时间是否超时或向您邮箱发送的验证连接的正确性";
        } else {
            User user = userDao.getUserByEmail(email);
            if ("0".equals(user.getIsVerification())) {
                String systemVerificationRule = registerRule(user);
                if (systemVerificationRule != null && systemVerificationRule.equals(verificationRule)) {
                    user.setIsVerification("1");
                    try {
                        if (userDao.updateUser(user) == 0) {
                            System.out.println("ERROR:用户注册验证邮箱时操作数据库错误");
                            promptMessage = "对不起，邮箱" + email + "在验证时操作数据库错误，请您稍后再试，给您带来的不便敬请谅解";
                        } else {
                            promptTitle = "验证成功";
                            promptMessage = "恭喜您，邮箱" + email + "验证成功！欢迎加入" + sysName + "！";
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR:用户注册验证邮箱时操作数据库错误，错误信息：" + e);

                    }
                } else {
                    promptMessage = "对不起，" + email + "的验证连接不正确，请您确认向您邮箱发送的验证连接的正确性";
                }
            } else {
                promptMessage = "对不起，" + email + "已被验证，请您确认向您邮箱发送的验证连接的正确性";
            }
        }
        result.accumulate("promptTitle", promptTitle);
        result.accumulate("promptMessage", promptMessage);
        return result;
    }
}
