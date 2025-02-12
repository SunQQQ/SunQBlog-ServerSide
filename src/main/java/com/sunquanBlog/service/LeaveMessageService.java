package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface LeaveMessageService {
//    public ApiResponse editHeartFelt(Integer id,String content,String writer);

    public ApiResponse createLeaveMessage(Map<String,Object> map,Integer accountId);

//    public ApiResponse deleteHeartFelt(int id);

    public ApiResponse getAllLeaveMessage(Integer id);
}
