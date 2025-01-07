package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface HeartFeltService {
    public ApiResponse editHeartFelt(String username, String password);

    public ApiResponse createHeartFelt(String username, String password,String email,String role);

    public ApiResponse deleteHeartFelt(String username);

    public ApiResponse getHeartFeltList();
}
