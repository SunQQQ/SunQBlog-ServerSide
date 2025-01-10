package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.HeartFelt;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.HeartFeltService;
import com.sunquanBlog.service.SysLoginService;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
public class HeartFeltController {
    @Autowired
    private HeartFeltService heartFeltService;
    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("/heartfeltList")
    public ApiResponse heartfeltList(HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return heartFeltService.getHeartFeltList(userId);
    }

    @PostMapping("/createHeartfelt")
    public ApiResponse createHeartfelt(@RequestBody Map<String,String> requestBody,HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        String content = requestBody.get("content");
        String writer = requestBody.get("writer");
        
        return heartFeltService.createHeartFelt(content,writer,userId);
    }

    @PostMapping("/deleteHeartFelt")
    public ApiResponse deleteHeartFelt(@RequestBody Map<String,Integer> requestBody){

        return heartFeltService.deleteHeartFelt(requestBody.get("id"));
    }

    @PostMapping("/editHeartfelt")
    public ApiResponse editHeartfelt(@RequestBody HeartFelt requestBody){
        int id = requestBody.getId();
        String content = requestBody.getContent();
        String writer = requestBody.getWriter();
        String creater = requestBody.getCreater();

        return ApiResponse.success(heartFeltService.editHeartFelt(id,content,writer,creater));
    }
}


