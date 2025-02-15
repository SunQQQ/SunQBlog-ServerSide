package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.BlogService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
