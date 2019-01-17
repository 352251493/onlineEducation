package com.gxg.utils;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件相关工具类
 * @author 郭欣光
 * @date 2019/1/16 15:09
 */
public class FileUtils {

    /**
     * 从文件内容判断文件是否为图片
     * @param file 文件
     * @return 结果
     * @throws Exception 执行中可能存在的异常
     * @author 郭欣光
     */
    public static boolean isImage(MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据文件后缀判断文件是否是图片
     * @param fileType 文件后缀
     * @return 结果
     * @author 郭欣光
     */
    public static boolean isImageByType(String fileType) {
        boolean isImage = true;
        if (!"bmf".equals(fileType.toLowerCase()) && !"png".equals(fileType.toLowerCase()) && !"gif".equals(fileType) && !"jpg".equals(fileType) && !"jpeg".equals(fileType)) {
            isImage = false;
        }
        return isImage;
    }

    /**
     * 获取文件大小
     * @param file 文件
     * @return 文件大小（以字节为单位）
     * @author 郭欣光
     */
    public static long getFileSize(MultipartFile file) {
        return file.getSize();
    }

    /**
     * 上传文件
     * @param file  要上传文件
     * @param name 上传后的文件名称
     * @param path 上传后的文件路径
     * @return 上传结果
     * @author 郭欣光
     */
    public static JSONObject uploadFile(MultipartFile file, String name, String path) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "上传失败！";
        if(file.isEmpty()) {
            content = "文件为空！";
        } else {
            try {
                File uploadFilePath = new File(path);
                if (!uploadFilePath.exists()) {
                    uploadFilePath.mkdirs();
                }
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path, name)));
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                status = "true";
                content = "上传成功！";
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                content =  "上传失败：" + e.getMessage();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                content = "上传失败：" + e.getMessage();
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }

    /**
     * 删除文件
     * @param filePath 文件完整路径
     * @return 删除文件结果
     * @author 郭欣光
     */
    public static JSONObject deleteFile(String filePath) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        File file = new File(filePath);
        if(file.exists() && file.isFile()) {
            if(file.delete()) {
                status = "true";
                content =  "删除成功！";
            } else {
                content = "删除文件失败！";
            }
        } else {
            content = "删除文件失败：文件不存在！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }
}
