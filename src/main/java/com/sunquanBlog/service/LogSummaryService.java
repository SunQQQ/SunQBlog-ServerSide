package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;

import java.util.List;
import java.util.Map;

public interface LogSummaryService {

    public ApiResponse<Map> getOldUser(Integer days);
    public ApiResponse<List<Map>> getPlatFormRatio(Integer days);
}
