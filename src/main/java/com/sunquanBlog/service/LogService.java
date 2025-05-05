package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogIpDailyDTO;
import com.sunquanBlog.model.LogTerminalDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LogService{
    public Integer createLog(HttpServletRequest request,String platformType, String page, String action, String actionObject,String actionDesc,Integer userId);

    public ApiResponse<Map> getLogIp();

    public ApiResponse<List<LogDTO>> getUserAction(Integer day, HttpServletRequest request);

    public ApiResponse<List<LogIpDailyDTO>> getIpDaily(Integer days,HttpServletRequest request);

    public ApiResponse<String[]> getCityDaily(Integer days,HttpServletRequest request); // 获取IP所在城市的方法

    public ApiResponse<LogTerminalDTO> getTerminal(Integer days,HttpServletRequest request); // 获取终端类型的方法

    public ApiResponse<Map> getPageDaily(Integer days) ;

    public String getLocation(HttpServletRequest request); // 获取IP所在城市的方法

    public String getClientIpAddress(HttpServletRequest request); // 获取客户端真实IP地址的方法
}
