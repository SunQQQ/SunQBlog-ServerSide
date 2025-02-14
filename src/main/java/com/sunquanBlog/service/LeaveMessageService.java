package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface LeaveMessageService {
    public ApiResponse updateLeaveMessage(Map<String,Object> map);

    public ApiResponse updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName);

    public ApiResponse createLeaveMessage(Map<String,Object> map,Integer accountId);

    public ApiResponse deleteLeaveMessage(int id);

    public ApiResponse getAllLeaveMessage(Integer id);
}
