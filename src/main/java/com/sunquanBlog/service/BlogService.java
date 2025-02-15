package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {
    public ApiResponse getBlogList(Integer id);
}
