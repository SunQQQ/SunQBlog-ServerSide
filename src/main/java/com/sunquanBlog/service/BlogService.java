package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public interface BlogService {
    public ApiResponse getBlogList(Integer userId);

    public ApiResponse getBlogDetail(Integer blogId);

    public ApiResponse insertBlog(Map<String, Object> params);

    public ApiResponse deleteBlog(Integer id);

    public ApiResponse updateBlog(Map<String, Object> params);
}
