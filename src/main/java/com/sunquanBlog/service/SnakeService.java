package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Snake;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SnakeService {
    Map<String,Object> getSnakeScoreList(Integer start, Integer size);

    List<Snake> getSnakeScoreTopList(Integer topNum);

    Long getScoreListCount();

    Integer createScore(Snake snake);

    Integer scoreMulDelete(List ids);
}
