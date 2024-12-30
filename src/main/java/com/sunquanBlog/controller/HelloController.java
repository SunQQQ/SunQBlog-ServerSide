package com.sunquanBlog.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunquanBlog.model.user;
import com.sunquanBlog.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import com.sunquanBlog.common.util.ApiResponse;

import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private IndexService indexService;

    @GetMapping("/hello")
    public ApiResponse<List<user>> sayHello() {
        try {
            List<user> indices = indexService.getAll();
            return ApiResponse.success(indices);
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            throw e; // 或者返回自定义错误消息
        }
    }
}
