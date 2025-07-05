package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.FriendUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FriendUrlController {
    @Autowired
    public FriendUrlService friendUrlService;

    @PostMapping("/getSiteList")
    public ApiResponse getSiteList(@RequestBody Map<String,Object> requestBody) {
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        return friendUrlService.getFriendUrlList(start, size);
    }
}
