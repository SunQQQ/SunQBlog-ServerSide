package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.SnakeMapper;
import com.sunquanBlog.model.Snake;
import com.sunquanBlog.service.SnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnakeServiceImpl implements SnakeService {
    @Autowired
    private SnakeMapper snakeMapper;

    @Override
    public List<Snake> getSnakeScoreList() {
        return snakeMapper.getScoreList();
    }

    @Override
    public List<Snake> getSnakeScoreTopList(Integer topNum) {
        return snakeMapper.getScoreTopList(topNum);
    }

    @Override
    public Long getScoreListCount() {
        return snakeMapper.getScoreListCount();
    }

    @Override
    public Integer createScore(Snake snake) {
        Integer result = snakeMapper.createScore(snake);
        return result;
    }
}
