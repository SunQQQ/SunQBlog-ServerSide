package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Log;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Mapper
public interface LogSummaryMapper {
    @MapKey("ip")
    List<Map> getTodayOldUser(Integer days);
    @MapKey("ip")
    List<Map> getOldUser(Integer day);

    @MapKey("platformType")
    List<Map> getTodayPlatformRatio(Integer days);

    @MapKey("platformType")
    List<Map> getPlatformRatio(Integer days);

    int deleteToday();

    void insertDailyIpSummary(
            @Param("visitDay") String visitDay,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("excludeIpsSql") String excludeIpsSql
    );

    int cleanAll();

    Map<String,Object> getTodayIp();

    Map<String,Object> getTotalIp();
}
