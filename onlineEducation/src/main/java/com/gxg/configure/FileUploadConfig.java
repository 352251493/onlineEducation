package com.gxg.configure;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传相关配置
 * @author 郭欣光
 * @date 2019/1/16 17:42
 */
@Configuration
public class FileUploadConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //设置文件大小限制，超出设置页面会抛出异常信息
        factory.setMaxFileSize("1024MB");//KB、MB
        //设置总上传数据的总大小
        factory.setMaxRequestSize("7168MB");
        return factory.createMultipartConfig();
    }

}
