package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.user;
import com.sunquanBlog.service.SysLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 登录接口
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ApiResponse<user> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return sysLoginService.checkLogin(username, password);
    }

    /**
     * 注册接口
     * @param loginRequest
     * @return
     */
    @PostMapping("/register")
    public ApiResponse register(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return sysLoginService.register(username, password);
    }

    /**
     * 删除账号接口
     * @param deleteRequest
     * @return
     */
    @PostMapping("/deleteByUser")
    public ApiResponse deleteByUser(@RequestBody Map<String, String> deleteRequest) {
        String username = deleteRequest.get("username");

        return sysLoginService.deleteByUsername(username);
    }

    /**
     * 所有账号的列表
     * @param roleReq
     * @return
     */
    @PostMapping("/userList")
    public ApiResponse userList(@RequestBody Map<String,String> roleReq){
        String role = roleReq.get("role");
        String username = roleReq.get("username");

        return sysLoginService.getAllUser(role,username);
    }
}
