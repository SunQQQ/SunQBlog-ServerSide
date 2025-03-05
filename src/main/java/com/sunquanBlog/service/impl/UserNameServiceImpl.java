package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.mapper.UserNameMapper;
import com.sunquanBlog.service.UserNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNameServiceImpl implements UserNameService {
    @Autowired
    private UserNameMapper userNameMapper;
    @Override
    public ApiResponse getUserName() {
        return ApiResponse.success(userNameMapper.getOneName());
    }

    @Override
    public ApiResponse markNameAsUsed(Integer id) {
        return ApiResponse.success(userNameMapper.markNameAsUsed(id));
    }
}
