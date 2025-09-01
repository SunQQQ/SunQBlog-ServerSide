package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.LogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogSummaryController {
    @Autowired
    private LogSummaryService logSummaryService;

    @PostMapping("/old-users")
    public ApiResponse getOldUser(){
        return logSummaryService.getOldUser();
    }
}
