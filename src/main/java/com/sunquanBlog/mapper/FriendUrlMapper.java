package com.sunquanBlog.mapper;
import com.sunquanBlog.model.FriendUrl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendUrlMapper {
    // 添加友链
    int addSite(FriendUrl friendUrl, Integer userId);

    // 删除友链
    int deleteSite(Integer siteId);

    // 获取友链列表
    List<FriendUrl> getSiteList(int start,int size);

    List<FriendUrl> getAdminSiteList(int start,int size, int userId,String role);

    Integer getAdminListTotal(int userId, String role);

    // 更新友链
//    int updateFriendUrl(int id, String url, String description);
}
