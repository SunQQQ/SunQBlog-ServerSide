package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import com.sunquanBlog.model.User;
import org.springframework.stereotype.Service;

@Service
public interface SysLoginService {
    public ApiResponse<UserAuthResponse> checkLogin(String username, String password);

    public ApiResponse<String> register(String username, String password,String email,String role);

    public ApiResponse deleteByUsername(String username);

    public ApiResponse getAllUser(Integer userId);

    public User getUserById(Integer id);
}
