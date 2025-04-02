package com.sunquanBlog.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface LogMapper {
    Integer insertLog(String ip,String platformType,String page,String ipCity,String browser,String action,String actionObject,String actionDesc);
    @MapKey("ip")
    Map<String,Long> getTodayIp();

    @MapKey("ip")
    Map<String,Long> getTotalIp();
}
