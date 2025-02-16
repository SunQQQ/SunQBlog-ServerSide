package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.UserAuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface HeartFeltService {
    public ApiResponse editHeartFelt(Integer id,String content,String writer);

    public ApiResponse createHeartFelt(String content,String writer,Integer creater);

    public ApiResponse deleteHeartFelt(int id);

    public ApiResponse getHeartFeltList(Integer id);

    public ApiResponse getHeartFeltList();
}
