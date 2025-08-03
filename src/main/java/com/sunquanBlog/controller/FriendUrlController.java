package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.FriendUrl;
import com.sunquanBlog.service.FriendUrlService;
import com.sunquanBlog.service.LogService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class FriendUrlController {
    @Autowired
    public FriendUrlService friendUrlService;

    @Autowired
    public LogService logService;

    @PostMapping("/getSiteList")
    public ApiResponse getSiteList(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        logService.createLog(request, "用户端","朋友圈", "打开", "朋友圈页面","");

        return friendUrlService.getFriendUrlList(start, size);
    }

    @PostMapping("/getAdminSiteList")
    public ApiResponse getAdminSiteList(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        return friendUrlService.getAdminSiteList(start, size,userId);
    }

    @PostMapping("/addSite")
    public ApiResponse addSite(@RequestBody FriendUrl friendUrl, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return friendUrlService.addSite(friendUrl,userId);
    }

    @PostMapping("/deleteSite")
    public ApiResponse deleteSite(@RequestBody FriendUrl friendUrl, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        Integer siteId = friendUrl.getId();

        return friendUrlService.deleteSite(userId,siteId);
    }

    @PostMapping("/editSite")
    public ApiResponse editSite(@RequestBody FriendUrl friendUrl) {

        return friendUrlService.editSite(friendUrl);
    }
}
