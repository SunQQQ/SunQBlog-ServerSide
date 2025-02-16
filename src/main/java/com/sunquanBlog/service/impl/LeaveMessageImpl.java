package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LeaveMessageMapper;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.LeaveMessage;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.LeaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveMessageImpl implements LeaveMessageService {
    @Autowired
    private LeaveMessageMapper leaveMessageMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Override
    public ApiResponse updateLeaveMessage(Map<String,Object> map) {
        int updateNum = leaveMessageMapper.updateLeaveMessage(map);
        if(updateNum == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }

    @Override
    public ApiResponse updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName) {
        int updateNum = leaveMessageMapper.updateLeaveMessage(id,messageContent,city,avator,parentId,leaveName);
        if(updateNum == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }

    @Override
    public ApiResponse createLeaveMessage(Map<String,Object> map, Integer accountId) {
        int createNum = leaveMessageMapper.createLeaveMessage(map,accountId);
        if(createNum == 1){
            return ApiResponse.success("创建成功");
        }else {
            return ApiResponse.error(500,"创建失败，请留言");
        }
    }

    @Override
    public ApiResponse deleteLeaveMessage(int id) {
        if(leaveMessageMapper.deleteLeaveMessage(id) == 1){
            return ApiResponse.success("删除成功");
        }else {
            return ApiResponse.error(500,"删除失败");
        }
    }

    @Override
    public ApiResponse getAllLeaveMessage(Integer id) {
        User user = loginMapper.getUserById(id);
        List<LeaveMessage> heartFeltList = leaveMessageMapper.getAllLeaveMessage(id,user.getRole());
        return ApiResponse.success(heartFeltList);
    }

    @Override
    public ApiResponse getAllLeaveMessage() {
        List<LeaveMessage> heartFeltList = leaveMessageMapper.getuserSideMsg();
        return ApiResponse.success(heartFeltList);
    }
}
