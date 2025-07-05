package com.sunquanBlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class SunqBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunqBlogApplication.class, args);
        log.info("项目已启动成功");
    }
}
