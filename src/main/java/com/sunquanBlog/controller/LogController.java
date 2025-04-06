package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 获取当日和累计访问的ip/pv量
     * @return
     */
    @PostMapping("/getLogIp")
    public ApiResponse getTimeLineList(HttpServletRequest request) {
        // 记录打开访问统计页日志
        logService.createLog(request,"用户端","访问统计","打开","访问统计页","");

        return logService.getLogIp();
    }

    @PostMapping("/getUserAction")
    public ApiResponse getUserAction(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer day  = (Integer) requestBody.get("day");
        return logService.getUserAction(day,request);
    }

    @PostMapping("/ip-daily")
    public ApiResponse getIpDaily(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logService.getIpDaily(days,request);
    }

    @PostMapping("/city-daily")
    public ApiResponse getCityDaily(@RequestBody Map<String,Object> requestBody) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logService.getCityDaily(days);
    }

    @PostMapping("/getTerminal")
    public ApiResponse getTerminal(@RequestBody Map<String,Object> requestBody) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logService.getTerminal(days);
    }

    @PostMapping("/getPageDaily")
    public ApiResponse getPageDaily(@RequestBody Map<String,Object> requestBody) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logService.getPageDaily(days);
    }

    @PostMapping("/createLog")
    public ApiResponse createLog(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        String platformType = (String) requestBody.get("platformType");
        String page = (String) requestBody.get("page");
        String action = (String) requestBody.get("action");
        String actionObject = (String) requestBody.get("actionObject");
        String actionDesc = (String) requestBody.get("actionDesc");

        Integer status = logService.createLog(request,platformType,page,action,actionObject,actionDesc);
        if(status == 1) {
            return ApiResponse.success("日志记录成功");
        }else {
            return ApiResponse.error(400,"日志记录失败");
        }
    }
}
