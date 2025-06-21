package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LogMapper;
import com.sunquanBlog.model.Log;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogIpDailyDTO;
import com.sunquanBlog.model.LogTerminalDTO;
import com.sunquanBlog.service.LogService;
import org.springframework.beans.factory.DisposableBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunquanBlog.service.CityNameConverter;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements LogService, DisposableBean {
    @Autowired
    private CityNameConverter cityNameConverter;
    @Autowired
    private LogMapper logMapper;

    @Value("classpath:ip2location/IP2LOCATION-LITE-DB3.IPV6.BIN") // 默认classpath路径
    private Resource dbResource;

    @Override
    public Integer createLog(HttpServletRequest request, String platformType, String page, String action, String actionObject, String actionDesc, Integer userId) {
        // 获取客户端真实IP地址
        String ip = getClientIpAddress(request);
        // 获取浏览器信息
        String userAgent = isMobileDevice(request) ? "Mobile" : "PC";
        // 获取IP所在城市（需要调用第三方服务或本地IP库）
        String city = getCityByIp2(ip);

        // 插入日志
        Integer result = logMapper.insertLog(ip, platformType, page, city, userAgent, action, actionObject, actionDesc,userId);
        return result;
    }

    @Override
    public String getLocation(HttpServletRequest request) {
        // 获取客户端真实IP地址
        String ip = getClientIpAddress(request);
        // 获取IP所在城市（需要调用第三方服务或本地IP库）
        String city = getCityByIp2(ip);

        // 插入日志
        return city;
    }

    private static Path cachedDbFile = null;
    private static final Object lock = new Object();

    // 获取客户端真实IP地址
    public String getClientIpAddress(HttpServletRequest request) {
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

    public String getCityByIp2(String ip) {
        if (ip == null || ip.isEmpty() || ip.equals("127.0.0.1")) {
            return "Localhost";
        }

        // 初始化数据库文件（线程安全）
        synchronized (lock) {
            if (cachedDbFile == null || !Files.exists(cachedDbFile)) {
                try {
                    cachedDbFile = Files.createTempFile("ipdb", ".BIN");
                    try (InputStream in = dbResource.getInputStream()) {
                        Files.copy(in, cachedDbFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }
        }

        IP2Location loc = new IP2Location();
        try {
            loc.IPDatabasePath = cachedDbFile.toAbsolutePath().toString();
            IPResult result = loc.IPQuery(ip);
            return "OK".equals(result.getStatus()) ? cityNameConverter.convert(result.getCity()) : "Unknown";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            if (loc instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) loc).close();
                } catch (Exception e) {
                    // 记录日志
                }
            }
        }
    }

    // 添加关闭方法，在应用结束时调用
    public static void cleanup() {
        if (cachedDbFile != null) {
            try {
                Files.deleteIfExists(cachedDbFile);
            } catch (IOException e) {
                // 记录日志
            }
        }
    }
    @Override
    public void destroy() throws Exception {
        cleanup(); // 在 Spring 容器关闭时自动调用
    }

    @Override
    public ApiResponse<Map> getLogIp() {
        Log total = logMapper.getTotalIp();
        Log today = logMapper.getTodayIp();

        Map<String, Long> merged = new HashMap<>();
        merged.put("totalIpCount", total.getTotalIpCount());
        merged.put("totalPvCount", total.getTotalPvCount());
        merged.put("todayIpCount", today.getTodayIpCount());
        merged.put("todayPvCount", today.getTodayPvCount());

        return ApiResponse.success(merged);
    }

    @Override
    @Transactional // 保证两次调用在同一个数据库会话中
    public ApiResponse<List<LogDTO>> getUserAction(Integer day, HttpServletRequest request) {
        String ip = getClientIpAddress(request);

        logMapper.setGroupConcatMaxLen(); // 上调group、concat函数的长度限制
        List<LogDTO> logDTOs = logMapper.getUserAciton(day,day-1);

        for(int i=0;i<logDTOs.size();i++){

            if(logDTOs.get(i).getIp().equals(ip)){
                logDTOs.get(i).setIsCurUser(true);
            }else {
                logDTOs.get(i).setIsCurUser(false);
            }
        }

        if(!day.equals(0)){
            // 记录打开访问统计页日志
            createLog(request,"用户端","访问统计","切换","用户轨迹","：最近"+day+"天",0);
        }

        return ApiResponse.success(logDTOs);
    }

    @Override
    public ApiResponse<List<LogIpDailyDTO>> getIpDaily(Integer days,HttpServletRequest request) {
        List<LogIpDailyDTO> logDTOs = logMapper.getIpDaily(days);
        List<LogIpDailyDTO> regists = logMapper.getRegisterDaily(days);

        if(!days.equals(7)){
            // 记录打开访问统计页日志
            createLog(request,"用户端","访问统计","切换","流量趋势","：最近"+days+"天",0);
        }

        for(int i=0;i<logDTOs.size();i++){
            for(int j=0;j<regists.size();j++){
                if(logDTOs.get(i).getDay().equals(regists.get(j).getDay())){
                    logDTOs.get(i).setRegister(regists.get(j).getRegister());
                }
            }
        }

        return ApiResponse.success(logDTOs);
    }

    @Override
    public ApiResponse<String[]> getCityDaily(Integer days,HttpServletRequest request) {
        String citys = logMapper.getCityDaily(days);

        if(!days.equals(0)){
            // 记录打开访问统计页日志
            createLog(request,"用户端","访问统计","切换","访客来源","：最近"+days+"天",0);
        }

        String[] cityList = citys.split("\\|");
        return ApiResponse.success(cityList);
    }

    @Override
    public ApiResponse<LogTerminalDTO> getTerminal(Integer days,HttpServletRequest request) {
        LogTerminalDTO logTerminalDTO = logMapper.getTerminal(days);

        if(!days.equals(0)){
            // 记录打开访问统计页日志
            createLog(request,"用户端","访问统计","切换","数据占比","：最近"+days+"天",0);
        }

        return ApiResponse.success(logTerminalDTO);
    }

    @Override
    public ApiResponse<Map> getPageDaily(Integer days) {
        Map page = logMapper.getPageDaily(days);
        return ApiResponse.success(page);
    }
}
