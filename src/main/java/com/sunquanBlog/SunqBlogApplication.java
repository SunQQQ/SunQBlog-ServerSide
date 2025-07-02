package com.sunquanBlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SunqBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunqBlogApplication.class, args);
        log.info("项目已启动成功");
    }
}
