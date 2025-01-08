package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface HeartFeltService {
    public ApiResponse editHeartFelt(String username, String password);

    public ApiResponse createHeartFelt(String content,String writer,String creater);

    public ApiResponse deleteHeartFelt(int id);

    public ApiResponse getHeartFeltList();
}
