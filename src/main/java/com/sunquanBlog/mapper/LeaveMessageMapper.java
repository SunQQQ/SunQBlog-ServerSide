package com.sunquanBlog.mapper;

import com.sunquanBlog.model.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeaveMessageMapper {
    List<LeaveMessage> getAllLeaveMessage(Integer id, String role);

    int createLeaveMessage(Map<String,Object> params, Integer curId);
//
//    int deleteHeart(int id);
//
//    int updateHeartFelt(int id,String content,String writer);
}
