package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import com.sunquanBlog.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SysLoginService {
    public ApiResponse<UserAuthResponse> checkLogin(String username, String password);

    public ApiResponse<UserAuthResponse> regist(String username, String password,String email);

    public ApiResponse deleteByUsername(String username);

    public ApiResponse getAllUser(Integer userId,Integer start,Integer size);

    public User getUserById(Integer id);

    public ApiResponse<String> updateUser(Map<String,Object> map,Integer accountId);

    public ApiResponse<Map> getUserData();
}
