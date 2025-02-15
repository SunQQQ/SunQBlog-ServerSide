package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.BlogMapper;
import com.sunquanBlog.model.Blog;
import com.sunquanBlog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public ApiResponse getBlogList(Integer id){
        List<Blog> list = blogMapper.getBlogList(id,"");

        return ApiResponse.success(list);
    }
    @Override
    public ApiResponse insertBlog(Map<String, Object> params){
        Integer result = blogMapper.insertBlog(params);

        if(result > 0){
            return ApiResponse.success("发布成功");
        }else{
            return ApiResponse.error(500,"发布失败");
        }
    }
}
