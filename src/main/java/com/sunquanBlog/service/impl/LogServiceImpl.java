package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LogMapper;
import com.sunquanBlog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

import org.springframework.core.io.Resource; // 正确导入
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.sunquanBlog.service.CityNameConverter;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private CityNameConverter cityNameConverter;
    @Autowired
    private LogMapper logMapper;

    @Value("classpath:ip2location/IP2LOCATION-LITE-DB3.IPV6.BIN") // 默认classpath路径
    private Resource dbResource;

    @Override
    public Integer createLog(HttpServletRequest request,String platformType,String page, String action, String actionObject,String actionDesc) {
        // 获取客户端真实IP地址
        String ip = getClientIpAddress(request);
        // 获取浏览器信息
        String userAgent = isMobileDevice(request) ? "Mobile" : "PC";
        // 获取IP所在城市（需要调用第三方服务或本地IP库）
        String city = getCityByIp(ip);

        // 插入日志
        Integer result = logMapper.insertLog(ip,platformType,page,city,userAgent,action,actionObject,actionDesc);
        return result;
    }

    // 获取客户端真实IP地址
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();

        // 常见移动端关键词（按需补充）
        return userAgent.contains("mobile")
                || userAgent.contains("android")
                || userAgent.contains("iphone")
                || userAgent.contains("ipad")
                || userAgent.contains("windows phone");
    }

    // 获取 IP 所在城市
    public String getCityByIp(String ip) {
        if (ip == null || ip.isEmpty() || ip.equals("127.0.0.1")) {
            return "Localhost";
        }

//        IP2Location loc = new IP2Location();
        try {
            // 设置数据库路径（自动加载）
//            loc.IPDatabasePath =  dbResource.getFile().getAbsolutePath(); // 自动解析路径

            // 创建临时文件
            Path tempFile = Files.createTempFile("ipdb", ".BIN");
            try (InputStream in = dbResource.getInputStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 使用临时文件
            IP2Location loc = new IP2Location();
            loc.IPDatabasePath = tempFile.toAbsolutePath().toString();

            // 查询IP
            IPResult result = loc.IPQuery(ip);
            return "OK".equals(result.getStatus()) ? cityNameConverter.convert(result.getCity()) : "Unknown";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

}
