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

    public static final String MP4_H264 = "00000020667479706973";

    public static final String OGG = "4f676753000200000000";
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

    /**
     * 判断文件是否存在
     * @param fileName 文件名称
     * @return 结果
     * @author 郭欣光
     */
    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将内容写入文件
     * @param path 文件路径
     * @param name 文件名
     * @param content 文件内容
     * @return 写入结果
     * @author 郭欣光
     */
    public static JSONObject writeFile(String path, String name, String content) {
        JSONObject result = new JSONObject();
        String status = "false";
        String resultContent = "写入文件出错！";
        try {
            File fileDir = new File(path);
            if(!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(path + name);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte by[] = content.getBytes();
            fileOutputStream.write(by);
            fileOutputStream.close();
            status = "true";
            resultContent = "写入文件成功！";
        } catch (FileNotFoundException e) {
            System.out.println(e);
            resultContent = "写入文件出错：" + e.getMessage();
        } catch (IOException e) {
            System.out.println(e);
            resultContent = "写入文件出错：" + e.getMessage();
        }
        result.accumulate("status", status);
        result.accumulate("content", resultContent);
        return result;
    }

    /**
     * 读取文件
     * @param fileDir 文件路径
     * @return 读取结果
     * @author 郭欣光
     */
    public static JSONObject readFile(String fileDir) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "读取文件失败！";
        File file = new File(fileDir);
        if (file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileDir));
                String temp;
                String fileContent = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    fileContent += temp;
                    fileContent += "\n";
                }
                bufferedReader.close();
                status = "true";
                content = fileContent;
            } catch (Exception e) {
                content = "读取文件过程中出错！";
            }
        } else {
            content = "改文件不存在！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }


    /**
     * 判断文件类型是否为MP4（H264）或Ogg类型
     * @param file 文件
     * @return 结果
     * @throws Exception 异常信息
     */
    public static boolean isMp4H264OrOgg(MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            byte[] b = new byte[10];
            inputStream.read(b, 0, b.length);
            String fileCode = bytesToHexString(b);
            if (MP4_H264.equals(fileCode) || OGG.equals(fileCode)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获得文件头编码
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件类型判断是否为MP4(H264)或者Ogg类型
     * @param fileType 文件类型
     * @return 结果
     * @author 郭欣光
     */
    public static boolean isMp4H264OrOggByFileType(String fileType) {
        boolean isVideo = true;
        if (!"mp4".equals(fileType.toLowerCase()) && !"ogg".equals(fileType.toLowerCase()) && !"Ogg".equals(fileType)) {
            isVideo = false;
        }
        return isVideo;
    }
}
