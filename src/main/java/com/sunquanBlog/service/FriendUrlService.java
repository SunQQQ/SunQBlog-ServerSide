package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.FriendUrl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface FriendUrlService {
    public ApiResponse addSite(FriendUrl friendUrl,Integer userId);
    public ApiResponse deleteSite(Integer userId,Integer siteId);
    public ApiResponse getFriendUrlList(int start, int size);
    public ApiResponse getAdminSiteList(int start, int size,int userId);
    public ApiResponse updateFriendUrl(int id, String url, String description);
}
