package com.sunquanBlog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
@Mapper
public interface LogSummaryMapper {
    Map<String,Integer> getOldUser();
}
