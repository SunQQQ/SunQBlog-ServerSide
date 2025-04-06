package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogIpDailyDTO;
import com.sunquanBlog.model.LogTerminalDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LogService{
    public Integer createLog(HttpServletRequest request,String platformType, String page, String action, String actionObject,String actionDesc);

    public ApiResponse<Map> getLogIp();

    public ApiResponse<List<LogDTO>> getUserAction(Integer day, HttpServletRequest request);

    public ApiResponse<List<LogIpDailyDTO>> getIpDaily(Integer days);

    public ApiResponse<String[]> getCityDaily(Integer days); // 获取IP所在城市的方法

    public ApiResponse<LogTerminalDTO> getTerminal(Integer days); // 获取终端类型的方法
}
