package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.HeartFelt;
import com.sunquanBlog.service.HeartFeltService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HeartFeltController {
    @Autowired
    private HeartFeltService heartFeltService;

    @PostMapping("/heartfeltList")
    public ApiResponse heartfeltList(){

        return heartFeltService.getHeartFeltList();
    }

    @PostMapping("/createHeartfelt")
    public ApiResponse createHeartfelt(@RequestBody Map<String,String> requestBody){
        String content = requestBody.get("content");
        String writer = requestBody.get("writer");
        String creater = requestBody.get("creater");
        
        return heartFeltService.createHeartFelt(content,writer,creater);
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


