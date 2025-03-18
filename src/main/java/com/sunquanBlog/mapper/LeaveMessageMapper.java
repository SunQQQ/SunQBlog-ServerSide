package com.sunquanBlog.mapper;

import com.sunquanBlog.model.LeaveMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface LeaveMessageMapper {
    List<LeaveMessage> getAllLeaveMessage(Integer id, String role);

    List<LeaveMessage> getuserSideLevel1(Integer start,Integer size);

    // 获取二级留言数据
    List<LeaveMessage> getuserSideLevel2(List<Integer> parentIds);

    int createLeaveMessage(Map<String,Object> params, Integer curId,String userName);

    int deleteLeaveMessage(int id);

    int updateLeaveMessage(@Param("params") Map<String,Object> params);

    int updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName);

    int getLmCount();
}
