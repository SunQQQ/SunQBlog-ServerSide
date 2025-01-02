package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.user;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface SysLoginService {
    public ApiResponse<user> checkLogin(String username, String password);

    public ApiResponse<String> register(String username, String password);

    public ApiResponse deleteByUsername(String username);

    public ApiResponse getAllUser(String role,String username);
}
