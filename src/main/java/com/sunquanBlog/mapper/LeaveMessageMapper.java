package com.sunquanBlog.mapper;

import com.sunquanBlog.model.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeaveMessageMapper {
    List<LeaveMessage> getAllLeaveMessage(Integer id, String role);

    List<LeaveMessage> getuserSideMsg();

    int createLeaveMessage(Map<String,Object> params, Integer curId);

    int deleteLeaveMessage(int id);

    int updateLeaveMessage(@Param("params") Map<String,Object> params);

    int updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName);
}
