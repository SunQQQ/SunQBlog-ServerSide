package com.sunquanBlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://39.107.193.226",
                        "http://localhost:8082",
                        "http://localhost:8081",
                        "http://www.codinglife.online",
                        "http://codinglife.online",
                        "https://www.codinglife.online",
                        "https://codinglife.online")
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true)  // 允许携带凭证（如 Cookie）
                .maxAge(3600);  // 预检请求的缓存时间
    }
}