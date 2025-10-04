package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogSummary;
import com.sunquanBlog.model.LogTerminalDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LogSummaryService {

    public ApiResponse<Map> getOldUser(Integer days);
    public ApiResponse<List<Map>> getPlatFormRatio(Integer days);

    public ApiResponse<Map> getLogIp();

    public ApiResponse<List<LogSummary>> getIpDaily(Integer days, HttpServletRequest request);

    public ApiResponse<String[]> getCityDaily(Integer days,HttpServletRequest request); // 获取IP所在城市的方法

    public ApiResponse<LogTerminalDTO> getTerminal(Integer days, HttpServletRequest request); // 获取终端类型的方法

    public ApiResponse<List<LogDTO>> getUserAction(Integer day, HttpServletRequest request);
}
