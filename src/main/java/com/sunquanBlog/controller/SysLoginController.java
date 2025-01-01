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
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            HashMap loginStatus = sysLoginService.checkLogin(username, password);
            String status = (String) loginStatus.get("status");
            String message = (String) loginStatus.get("version");

            if(status == "success"){
                return ApiResponse.success("login success");
            }else {
                return ApiResponse.error(500,message);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            throw e; // 或者返回自定义错误消息
        }
    }

    /**
     * 注册接口
     * @param loginRequest
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResponse register(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            boolean haveAccount = sysLoginService.haveAccount(username);

            if(haveAccount){
                return ApiResponse.error(500,"账号已存在，请修改账号");
            }else {
                int insertNum = sysLoginService.register(username,password);
                if(insertNum > 0){
                    return ApiResponse.success("注册成功");
                }else {
                    return ApiResponse.error(500,"注册失败请联系博主");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误信息
            throw e; // 或者返回自定义错误消息
        }
    }


}
