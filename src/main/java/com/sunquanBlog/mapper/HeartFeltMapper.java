package com.sunquanBlog.mapper;

import com.sunquanBlog.model.HeartFelt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HeartFeltMapper {
    List<HeartFelt> getAllHeartFelt();
}
