package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.FriendUrl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface FriendUrlService {
    public ApiResponse addSite(FriendUrl friendUrl);
    public ApiResponse deleteFriendUrl(int id);
    public ApiResponse getFriendUrlList(int start, int size);
    public ApiResponse updateFriendUrl(int id, String url, String description);
}
