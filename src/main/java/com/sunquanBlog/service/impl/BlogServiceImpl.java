package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.BlogMapper;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.Blog;
import com.sunquanBlog.service.BlogService;
import com.sunquanBlog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private LogService logService;
    @Override
    public ApiResponse getBlogList(Integer userId,Integer tagId,Integer start,Integer size){
        String role = loginMapper.getUserById(userId).getRole();

        List<Blog> list = blogMapper.getBlogList(userId,role,tagId,start,size);
        Integer total = blogMapper.getBlogNumByRoleId(role,userId);

        Map<String,Object> result = new java.util.HashMap<>();
        result.put("list",list);
        result.put("total",total);

        return ApiResponse.success(result);
    }

    @Override
    public ApiResponse getUserBlogList(Integer tagId,Integer start,Integer size){
        List<Blog> list = blogMapper.getUserBlogList(tagId,start,size);
        Integer total = blogMapper.getBlogNum(tagId);

        Map<String,Object> result = new java.util.HashMap<>();
        result.put("list",list);
        result.put("total",total);

        return ApiResponse.success(result);
    }

    public ApiResponse getBlogDetail(Integer blogId, HttpServletRequest request){
        Blog blog = blogMapper.getBlogDetail(blogId);

        logService.createLog(request,"用户端", "文章详情页", "阅读", "文章","："+blog.getTitle(),0);

        if(blog != null){
            blogMapper.addBlogViewNum(blogId);
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

    public ApiResponse getHotList(Integer topNum){
        List<Blog> list = blogMapper.getHotList(topNum);

        return ApiResponse.success(list);
    }
}
