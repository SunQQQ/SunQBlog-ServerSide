package com.sunquanBlog.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface LogSummaryMapper {
    @MapKey("ip")
    List<Map> getTodayOldUser(Integer day);
    @MapKey("ip")
    List<Map> getOldUser(Integer day);
}
