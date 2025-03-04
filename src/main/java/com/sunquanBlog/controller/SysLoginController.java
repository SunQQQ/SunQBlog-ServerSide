package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.SysLoginService;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private HttpServletRequest commonRequest;
    /**
     * 登录接口
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ApiResponse<UserAuthResponse> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return sysLoginService.checkLogin(username, password);
    }

    /**
     * 注册接口
     * @param loginRequest
     * @return
     */
    @PostMapping("/regist")
    public ApiResponse regist(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String email = "test";

        return sysLoginService.regist(username, password,email);
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
     * @param request
     * @return
     */
    @PostMapping("/userList")
    public ApiResponse<String> userList(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return sysLoginService.getAllUser(userId);
    }

    @PostMapping("/updateUser")
    public ApiResponse<String> updateUser(@RequestBody Map<String, Object> req){
        // 当前登录账号的id
        Claims claims = (Claims) commonRequest.getAttribute("claims");
        Integer accountId = claims.get("id", Integer.class);

        return sysLoginService.updateUser(req,accountId);
    }
}
