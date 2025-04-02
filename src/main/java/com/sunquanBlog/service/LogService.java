package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LogService{
    public Integer createLog(HttpServletRequest request,String platformType, String page, String action, String actionObject,String actionDesc);

    public ApiResponse<Map> getLogIp();
}
