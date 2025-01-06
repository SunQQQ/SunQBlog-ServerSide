package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.HeartFeltService;
import org.springframework.stereotype.Service;

@Service
public class HeartFeltServiceImpl implements HeartFeltService {
    @Override
    public ApiResponse editHeartFelt(String username, String password) {
        return null;
    }

    @Override
    public ApiResponse createHeartFelt(String username, String password, String email, String role) {
        return null;
    }

    @Override
    public ApiResponse deleteHeartFelt(String username) {
        return null;
    }

    @Override
    public ApiResponse getHeartFelt(String role, String username) {
        return null;
    }
}
