package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.FriendUrlMapper;
import com.sunquanBlog.model.FriendUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FriendUrlServiceImpl implements com.sunquanBlog.service.FriendUrlService{
    @Autowired
    private FriendUrlMapper friendUrlMapper;

    @Override
    public ApiResponse addSite(FriendUrl friendUrl) {
//        if (friendUrl == null || friendUrl.isEmpty()) {
//            return ApiResponse.error(500,"参数不能为空");
//        }
//        if (!params.containsKey("siteName") || !params.containsKey("siteUrl") || !params.containsKey("siteDesc") || !params.containsKey("siteLogo")) {
//            return ApiResponse.error(500,"缺少必要的参数");
//        }
        int result = friendUrlMapper.addSite(friendUrl);
        if (result > 0) {
            return ApiResponse.success("友链添加成功");
        } else {
            return ApiResponse.error(500,"友链添加失败");
        }
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
