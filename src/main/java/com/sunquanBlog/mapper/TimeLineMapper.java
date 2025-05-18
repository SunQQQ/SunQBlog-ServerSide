package com.sunquanBlog.mapper;

import com.sunquanBlog.model.TimeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TimeLineMapper {
    List<TimeLine> getAllTimeLine();

    int deleteTimeLine(Integer id);
}
