package com.sunquanBlog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns("/login","/getDictionaryList","/getUserBlogList",
                "/getBlogDetail","/userLeaveMsgList","/userHeartList","/getUserName",
                "/regist","/UploadImg","/uploads/**","/getCommentList",
                        "/getTimeLineList","/getCommentCount","/getLmCount",
                        "/getTodayUser","/getUserData","/getLogIp","/getUserAction",
                        "/ip-daily","/city-daily","/getTerminal","/getPageDaily",
                        "/createLog","/getSnakeScoreList","/createScore","/getSnakeScoreTopList",
                        "/getWeather","/getHotList","/getSiteList","/getClientIpAddress");  // 排除登录和注册接口
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/** 映射到文件存储路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}