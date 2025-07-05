package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface FriendUrlService {
    public ApiResponse addFriendUrl(String url, String description);
    public ApiResponse deleteFriendUrl(int id);
    public ApiResponse getFriendUrlList(int start, int size);
    public ApiResponse updateFriendUrl(int id, String url, String description);
}
