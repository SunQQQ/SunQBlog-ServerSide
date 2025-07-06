package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.FriendUrlMapper;
import com.sunquanBlog.model.FriendUrl;
import com.sunquanBlog.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FriendUrlServiceImpl implements com.sunquanBlog.service.FriendUrlService{
    @Autowired
    private FriendUrlMapper friendUrlMapper;

    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public ApiResponse addSite(FriendUrl friendUrl, Integer userId) {
        int result = friendUrlMapper.addSite(friendUrl,userId);
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
    public ApiResponse getAdminSiteList(int start, int size, int userId) {
        String userRole = sysLoginService.getUserById(userId).getRole();
        List list = friendUrlMapper.getAdminSiteList(start,size,userId,userRole);
        Integer total = friendUrlMapper.getAdminListTotal(userId,userRole);

        Map<String, Object> result = new HashMap<>();
        result.put("list",list);
        result.put("total", total);

        return ApiResponse.success(result);
    }

    @Override
    public ApiResponse updateFriendUrl(int id, String url, String description) {
        return null;
    }
}
