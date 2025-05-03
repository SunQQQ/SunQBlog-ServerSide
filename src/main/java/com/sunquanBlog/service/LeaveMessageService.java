package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public interface LeaveMessageService {
    public ApiResponse updateLeaveMessage(Map<String,Object> map);

    public ApiResponse updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName);

    public ApiResponse createLeaveMessage(Map<String,Object> map, Integer accountId, HttpServletRequest request);

    public ApiResponse deleteLeaveMessage(int id);

    // 此方法用于留言列表 管理员端
    public ApiResponse getAllLeaveMessage(Integer id);

    // 此方法用于留言列表 用户端
    public ApiResponse getAllLeaveMessage(Integer start,Integer size);

    public ApiResponse getLmCount();

    public ApiResponse getWeather(HttpServletRequest request);
}
