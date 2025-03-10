package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserNameService {
    public ApiResponse getUserName();

    public ApiResponse markNameAsUsed(Integer id);
}
