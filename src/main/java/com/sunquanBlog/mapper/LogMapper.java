package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Log;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogIpDailyDTO;
import com.sunquanBlog.model.LogTerminalDTO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogMapper {
    Integer insertLog(String ip,String platformType,String page,String ipCity,String browser,String action,String actionObject,String actionDesc);

    Log getTodayIp();

    Log getTotalIp();

    // 根据时间段查询
    List<LogDTO> getUserAciton(Integer start,Integer end);

    // 根据ip查询
    List<LogIpDailyDTO> getIpDaily(Integer days);

    List<LogIpDailyDTO> getRegisterDaily(Integer days);

    String getCityDaily(Integer days); // 获取IP所在城市的方法

    LogTerminalDTO getTerminal(Integer days); // 获取终端类型的方法

    Map<String, Integer> getPageDaily(Integer days);
}
