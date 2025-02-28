package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.HeartFelt;
import com.sunquanBlog.service.LeaveMessageService;
import com.sunquanBlog.service.SysLoginService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LeaveMessageController {
    @Autowired
    private LeaveMessageService leaveMessageService;
    @Autowired
    private SysLoginService sysLoginService;

    // 留言列表 用于管理员端
    @PostMapping("/leaveMessageList")
    public ApiResponse leaveMessageList(HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return leaveMessageService.getAllLeaveMessage(userId);
    }

    // 用户端留言列表
    @PostMapping("/userLeaveMsgList")
    public ApiResponse userLeaveMsgList(@RequestBody Map<String,Object> requestBody){
        Integer start = (Integer)requestBody.get("start");
        Integer size = (Integer)requestBody.get("size");

        return leaveMessageService.getAllLeaveMessage(start,size);
    }

    @PostMapping("/createLeaveMessage")
    public ApiResponse createLeaveMessage(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        return leaveMessageService.createLeaveMessage(requestBody,userId);
    }

    @PostMapping("/deleteLeaveMessage")
    public ApiResponse deleteHeartFelt(@RequestBody Map<String,Integer> requestBody){

        return leaveMessageService.deleteLeaveMessage(requestBody.get("id"));
    }

    @PostMapping("/updateLeaveMessage")
    public ApiResponse updateLeaveMessage(@RequestBody Map<String,Object> requestBody){
        Integer id = (Integer) requestBody.get("id");
        String messageContent = (String) requestBody.get("messageContent");
        String city = (String) requestBody.get("city");
        String avator = (String) requestBody.get("avator");
        Integer parentId = (Integer) requestBody.get("parentId");
        String leaveName = (String) requestBody.get("leaveName");

        return leaveMessageService.updateLeaveMessage(id,messageContent,city,avator,parentId,leaveName);
    }
}


