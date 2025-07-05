package com.sunquanBlog.mapper;
import com.sunquanBlog.model.FriendUrl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendUrlMapper {
    // 添加友链
//    int addFriendUrl(String url, String description);
//
//    // 删除友链
//    int deleteFriendUrl(int id);

    // 获取友链列表
    List<FriendUrl> getSiteList();

    // 更新友链
//    int updateFriendUrl(int id, String url, String description);
}
