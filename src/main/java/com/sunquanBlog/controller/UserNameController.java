package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.SysLoginService;
import com.sunquanBlog.service.UserNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserNameController {
    @Autowired
    private UserNameService userNameService;

    @PostMapping("/getUserName")
    public ApiResponse getUserName() {
        return userNameService.getUserName();
    }

    @PostMapping("/markNameAsUsed")
    public ApiResponse markNameAsUsed(@RequestBody Map<String,Integer> requestBody) {
        Integer id = requestBody.get("id");
        return userNameService.markNameAsUsed(id);
    }
}
