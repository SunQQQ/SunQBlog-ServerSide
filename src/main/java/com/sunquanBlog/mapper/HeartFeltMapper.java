package com.sunquanBlog.mapper;

import com.sunquanBlog.model.HeartFelt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HeartFeltMapper {
    List<HeartFelt> getAllHeartFelt(Integer id,String role);
    List<HeartFelt> getUserHeart();

    int createHeartFelt(String content,String writer,Integer creater);

    int deleteHeart(int id);

    int updateHeartFelt(int id,String content,String writer);
}
