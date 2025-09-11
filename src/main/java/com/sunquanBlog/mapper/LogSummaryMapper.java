package com.sunquanBlog.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

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

    int deleteAll();
}
