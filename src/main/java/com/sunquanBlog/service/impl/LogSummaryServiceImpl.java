package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LogSummaryMapper;
import com.sunquanBlog.service.LogSummaryService;

import java.util.Map;

public class LogSummaryServiceImpl implements LogSummaryService {
    LogSummaryMapper logSummaryMapper;
    @Override
    public ApiResponse<Map> getOldUser(){
        Map oldUser = logSummaryMapper.getOldUser();
        return ApiResponse.success(oldUser);
    }
}
