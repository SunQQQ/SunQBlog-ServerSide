package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.BlogMapper;
import com.sunquanBlog.model.Blog;
import com.sunquanBlog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public ApiResponse getBlogList(Integer id){
        List<Blog> list = blogMapper.getBlogList(id,"");

        return ApiResponse.success(list);
    }
}
