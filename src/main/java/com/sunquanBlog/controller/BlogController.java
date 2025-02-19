package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.BlogService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class BlogController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/getBlogList")
    public ApiResponse getBlogList(HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return blogService.getBlogList(userId);
    }

    // 给用户端使用，无需登录
    @PostMapping("/getUserBlogList")
    public ApiResponse getUserBlogList(@RequestBody Map<String,Object> requestBody){
        Integer tagId = (Integer) requestBody.get("tag");
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        return blogService.getUserBlogList(tagId,start,size);
    }

    @PostMapping("/getBlogDetail")
    public ApiResponse getBlogDetail(@RequestBody Map<String,Object> requestBody){
        Integer blogId = (Integer) requestBody.get("id");

        return blogService.getBlogDetail(blogId);
    }

    @PostMapping("/createBlog")
    public ApiResponse createBlog(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);
        requestBody.put("userId",userId);

        return blogService.insertBlog(requestBody);
    }

    @PostMapping("/deleteBlog")
    public ApiResponse deleteBlog(@RequestBody Map<String,Object> requestBody){
        Integer blogId = (Integer) requestBody.get("id");

        return blogService.deleteBlog(blogId);
    }

    @PostMapping("/updateBlog")
    public ApiResponse updateBlog(@RequestBody Map<String,Object> requestBody){
        return blogService.updateBlog(requestBody);
    }
}
