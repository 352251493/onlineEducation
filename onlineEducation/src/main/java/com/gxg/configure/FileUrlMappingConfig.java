package com.gxg.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 磁盘资源与URL地址映射
 * @author 郭欣光
 * @date 2019/1/21 16:46
 */
@Configuration
public class FileUrlMappingConfig extends WebMvcConfigurerAdapter {

    @Value("${user.resource.dir}")
    private String userResourceDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user_resource/**").addResourceLocations("file:" + userResourceDir);
    }
}
