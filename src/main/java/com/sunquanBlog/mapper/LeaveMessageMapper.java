package com.sunquanBlog.mapper;

import com.sunquanBlog.model.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveMessageMapper {
    List<LeaveMessage> getAllLeaveMessage(Integer id, String role);

//    int createHeartFelt(String content,String writer,Integer creater);
//
//    int deleteHeart(int id);
//
//    int updateHeartFelt(int id,String content,String writer);
}
