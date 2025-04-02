package com.sunquanBlog.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    Integer insertLog(String ip,String platformType,String page,String ipCity,String browser,String action,String actionObject,String actionDesc);
}
