package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.LogSummary;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LogSummaryService {

    public ApiResponse<Map> getOldUser(Integer days);
    public ApiResponse<List<Map>> getPlatFormRatio(Integer days);

    public ApiResponse<Map> getLogIp();

    public ApiResponse<List<LogSummary>> getIpDaily(Integer days, HttpServletRequest request);

    public ApiResponse<String[]> getCityDaily(Integer days,HttpServletRequest request); // 获取IP所在城市的方法
}
