package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.HeartFeltMapper;
import com.sunquanBlog.service.HeartFeltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartFeltServiceImpl implements HeartFeltService {
    @Autowired
    private HeartFeltMapper heartFeltMapper;
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
    public ApiResponse getHeartFeltList() {

        return ApiResponse.success(heartFeltMapper.getAllHeartFelt());
    }
}
