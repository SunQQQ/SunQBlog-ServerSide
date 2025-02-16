package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.BlogMapper;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.Blog;
import com.sunquanBlog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public ApiResponse getBlogList(Integer userId){
        String role = loginMapper.getUserById(userId).getRole();

        List<Blog> list = blogMapper.getBlogList(userId,role);

        return ApiResponse.success(list);
    }

    @Override
    public ApiResponse getUserBlogList(Integer tagId){
        List<Blog> list = blogMapper.getUserBlogList(tagId);

        return ApiResponse.success(list);
    }

    public ApiResponse getBlogDetail(Integer blogId){
        Blog blog = blogMapper.getBlogDetail(blogId);

        if(blog != null){
            return ApiResponse.success(blog);
        }else{
            return ApiResponse.error(500,"查询失败");
        }
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
    @Override
    public ApiResponse deleteBlog(Integer id){
        Integer result = blogMapper.deleteBlog(id);

        if(result > 0){
            return ApiResponse.success("删除成功");
        }else{
            return ApiResponse.error(500,"删除失败");
        }
    }

    @Override
    public ApiResponse updateBlog(Map<String, Object> params){
        Integer result = blogMapper.updateBlog(params);

        if(result > 0){
            return ApiResponse.success("更新成功");
        }else{
            return ApiResponse.error(500,"更新失败");
        }
    }
}
