package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Snake;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SnakeMapper {
    List<Snake> getScoreList();

    Integer createScore(Snake snake);
}
