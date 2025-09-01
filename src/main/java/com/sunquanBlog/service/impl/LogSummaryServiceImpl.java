package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LogSummaryMapper;
import com.sunquanBlog.service.LogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class LogSummaryServiceImpl implements LogSummaryService {
    @Autowired
    LogSummaryMapper logSummaryMapper;
    @Override
    public ApiResponse<List<Map>> getOldUser(){
        List<Map> oldUser = logSummaryMapper.getOldUser();
        return ApiResponse.success(oldUser);
    }
}
