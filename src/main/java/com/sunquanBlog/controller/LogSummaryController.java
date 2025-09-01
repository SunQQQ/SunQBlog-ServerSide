package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.LogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogSummaryController {
    @Autowired
    private LogSummaryService logSummaryService;

    @PostMapping("/old-users")
    public ApiResponse getOldUser(@RequestBody Map<String,Object> requestBody){
        Integer day  = (Integer) requestBody.get("day");
        return logSummaryService.getOldUser(day);
    }
}
