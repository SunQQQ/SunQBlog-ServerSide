package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.HeartFeltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartFeltController {
    @Autowired
    private HeartFeltService heartFeltService;

    @PostMapping("/heartfeltList")
    public ApiResponse heartfeltList(){

        return heartFeltService.getHeartFeltList();
    }
}


