package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/getLogIp")
    public ApiResponse getTimeLineList(HttpServletRequest request) {
        // 记录日志
        return logService.getLogIp();
    }

    @PostMapping("/getUserAction")
    public ApiResponse getUserAction(HttpServletRequest request) {
        // 记录日志
        return logService.getUserAction();
    }
}
