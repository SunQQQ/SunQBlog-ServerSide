package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.job.LogSummaryJob;
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
    @Autowired
    private LogSummaryJob logSummaryJob;

    @PostMapping("/getPageDaily")
    public ApiResponse getPageDaily(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
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

    // 获取当前IP的所在城市
    @PostMapping("/getLocation")
    public ApiResponse getLocation(HttpServletRequest request) {
        String location = logService.getLocation(request);
        return ApiResponse.success(location);
    }

    /**
     * 手工归集某一天的ip访问数据
     */
    @PostMapping("/manualProcessDate")
    public void getClientIpAddress() {
        logSummaryJob.manualProcessDate("2025-08-27");
    }
}
