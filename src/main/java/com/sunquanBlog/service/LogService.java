package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface LogService{
    public Integer createLog(HttpServletRequest request,String platformType, String page, String action, String actionObject,String actionDesc);
}
