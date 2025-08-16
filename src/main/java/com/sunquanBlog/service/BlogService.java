package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public interface BlogService {
    public ApiResponse getBlogList(Integer userId,Integer tagId,Integer start,Integer size);

    public ApiResponse getUserBlogList(Integer[] tagId, Integer start, Integer size);

    public ApiResponse getBlogDetail(Integer blogId, HttpServletRequest request);

    public ApiResponse insertBlog(Map<String, Object> params);

    public ApiResponse deleteBlog(Integer id);

    public ApiResponse updateBlog(Map<String, Object> params);

    public ApiResponse getHotList(Integer topNum);
}
