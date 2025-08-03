package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Log;
import com.sunquanBlog.model.LogDTO;
import com.sunquanBlog.model.LogIpDailyDTO;
import com.sunquanBlog.model.LogTerminalDTO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogMapper {
    Integer insertLog(String ip,String platformType,String page,String ipCity,String browser,String action,String actionObject,String actionDesc,Integer userId);

    Log getTodayIp();

    Log getTotalIp();

    // 改为直接写在mysql配置项里了，后期稳定后代码可删除
    // 设置 group_concat_max_len
    // @Update("SET SESSION group_concat_max_len = 1000000")
    // void setGroupConcatMaxLen();
    List<String> getWhiteListIP(Integer userId, Integer start,Integer end);

    // 根据时间段查询
//    List<LogDTO> getUserAction(Integer start,Integer end,List<String> list);
    List<LogDTO> getUserAction(
            @Param("start") Integer start,
            @Param("end") Integer end,
            @Param("excludeIps") String excludeIps);

    // 根据ip查询
    List<LogIpDailyDTO> getIpDaily(Integer days);

    List<LogIpDailyDTO> getRegisterDaily(Integer days);

    String getCityDaily(Integer days); // 获取IP所在城市的方法

    LogTerminalDTO getTerminal(Integer days); // 获取终端类型的方法

    Map<String, Integer> getPageDaily(Integer days);
}
