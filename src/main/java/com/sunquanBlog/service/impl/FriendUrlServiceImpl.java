package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.FriendUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendUrlServiceImpl implements com.sunquanBlog.service.FriendUrlService{
    @Autowired
    private FriendUrlMapper friendUrlMapper;

    @Override
    public ApiResponse addFriendUrl(String url, String description) {
        return null;
    }

    @Override
    public ApiResponse deleteFriendUrl(int id) {
        return null;
    }

    @Override
    public ApiResponse getFriendUrlList(int start, int size) {
        return ApiResponse.success(friendUrlMapper.getSiteList(start,size));
    }

    @Override
    public ApiResponse updateFriendUrl(int id, String url, String description) {
        return null;
    }
}
