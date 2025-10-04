package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.IpMasker;
import com.sunquanBlog.mapper.LogMapper;
import com.sunquanBlog.mapper.LogSummaryMapper;
import com.sunquanBlog.model.*;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.LogSummaryService;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogSummaryServiceImpl implements LogSummaryService {
    @Autowired
    LogSummaryMapper logSummaryMapper;
    @Autowired
    LogMapper logMapper;
    @Autowired
    LogService logService;
    @Override
    public ApiResponse<Map> getOldUser(Integer days){
        Integer oldUser = 0;
        Integer newUser = 0;
        List<Map> ipData;

        if(days == 1){
            ipData = logSummaryMapper.getTodayOldUser(days);
        }else {
            ipData = logSummaryMapper.getOldUser(days);
        }

        for (Map user : ipData) {
            String type = (String) user.get("user_type");
            if (type.equals("新用户")) {
                newUser++;
            } else {
                oldUser++;
            }
        }

        Map<String,Number> result = new HashMap<>();
        result.put("oldUser", oldUser);
        result.put("newUser", newUser);
//        result.put("total", oldUser + newUser);

        return ApiResponse.success(result);
    }

    /**
     * 获取平台访问比例
     * @param days
     * @return
     */
    @Override
    public ApiResponse<List<Map>> getPlatFormRatio(Integer days) {
        List<Map> platformData;
        if (days == 1) {
            platformData = logSummaryMapper.getTodayPlatformRatio(days);
        } else {
            platformData = logSummaryMapper.getPlatformRatio(days);
        }

        return ApiResponse.success(platformData);
    }

    /**
     * 获取总访问ip和pv
     * 从日志汇总表里查，该表为每天的ip访问汇总。同一ip跨天访问多次，算多次pv，也算多次ip，视为老用户行为。
     * @return
     */
    @Override
    public ApiResponse<Map> getLogIp() {
        Map<String,Object> total = logSummaryMapper.getTotalIp();
        Map<String,Object> today = logSummaryMapper.getTodayIp();

        Map<String,Long> result = new HashMap<>();

        result.put("totalIpCount",((Number)total.get("totalIpCount")).longValue());
        result.put("totalPvCount",((Number)total.get("totalPvCount")).longValue());
        result.put("todayIpCount",((Number)today.get("todayIpCount")).longValue());
        result.put("todayPvCount",((Number)today.get("todayPvCount")).longValue());

        return ApiResponse.success(result);
    }

    @Override
    public ApiResponse<List<LogSummary>> getIpDaily(Integer days, HttpServletRequest request) {
        List<LogSummary> logDTOs = logSummaryMapper.getIpDaily(days);
        List<LogIpDailyDTO> regists = logMapper.getRegisterDaily(days);

        if(!days.equals(8)){
            // 记录打开访问统计页日志
            logService.createLog(request,"用户端","访问统计","切换","流量趋势","：最近"+days+"天");
        }

        for(int i=0;i<logDTOs.size();i++){
            for(int j=0;j<regists.size();j++){
                if(logDTOs.get(i).getVisitDay().equals(regists.get(j).getDay())){
                    logDTOs.get(i).setRegister(regists.get(j).getRegister());
                }
            }
        }

        return ApiResponse.success(logDTOs);
    }

    @Override
    public ApiResponse<String[]> getCityDaily(Integer days,HttpServletRequest request) {
        String citys = logSummaryMapper.getCityDaily(days);

        if(!days.equals(0)){
            // 记录打开访问统计页日志
            logService.createLog(request,"用户端","访问统计","切换","访客来源","：最近"+days+"天");
        }

        String[] cityList = citys.split("\\|");
        return ApiResponse.success(cityList);
    }

    @Override
    public ApiResponse<LogTerminalDTO> getTerminal(Integer days, HttpServletRequest request) {
        LogTerminalDTO logTerminalDTO = logSummaryMapper.getTerminal(days);

        if(!days.equals(1)){
            // 记录打开访问统计页日志
            logService.createLog(request,"用户端","访问统计","切换","数据占比","：最近"+days+"天");
        }

        return ApiResponse.success(logTerminalDTO);
    }

    @Override
    @Transactional // 保证两次调用在同一个数据库会话中
    public ApiResponse<List<LogDTO>> getUserAction(Integer day, HttpServletRequest request) {
        String ip = logService.getClientIpAddress(request);

        // 查询用户轨迹
        List<LogDTO> logDTOs = logSummaryMapper.getUserAction(day,day-1);

        for(int i=0;i<logDTOs.size();i++){
            // 脱敏IP地址
            String curIp = logDTOs.get(i).getIp();
            logDTOs.get(i).setIp(IpMasker.mask(curIp));

            // 标记当前用户
            if(logDTOs.get(i).getIp().equals(ip)){
                logDTOs.get(i).setIsCurUser(true);
            }else {
                logDTOs.get(i).setIsCurUser(false);
            }
        }

        if(!day.equals(0)){
            // 记录打开访问统计页日志
            logService.createLog(request,"用户端","访问统计","切换","用户轨迹","：最近"+day+"天");
        }

        return ApiResponse.success(logDTOs);
    }
}
