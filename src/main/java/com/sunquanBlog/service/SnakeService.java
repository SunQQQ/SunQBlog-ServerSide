package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Snake;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SnakeService {
    List<Snake> getSnakeScoreList();

    List<Snake> getSnakeScoreTopList(Integer topNum);

    Long getScoreListCount();

    Integer createScore(Snake snake);
}
