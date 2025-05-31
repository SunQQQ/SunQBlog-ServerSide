package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.SnakeMapper;
import com.sunquanBlog.model.Snake;
import com.sunquanBlog.service.SnakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SnakeServiceImpl implements SnakeService {
    @Autowired
    private SnakeMapper snakeMapper;

    @Override
    public Map<String,Object> getSnakeScoreList(Integer start, Integer size) {
        Map<String,Object> map = new HashMap<>();
        Long total = getScoreListCount();
        map.put("total", total);
        map.put("list", snakeMapper.getScoreList(start,size));

        return map;
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

    @Override
    public Integer scoreMulDelete(List ids) {
        return snakeMapper.scoreMulDelete(ids);
    }
}
