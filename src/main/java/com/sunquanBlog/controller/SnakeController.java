package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Snake;
import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.SnakeService;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SnakeController {
    @Autowired
    private SnakeService snakeService;
    @Autowired
    private LogService logService;

    @PostMapping("/getSnakeScoreList")
    public ApiResponse getSnakeScoreList() {
        List<Snake> snakeScoreList = snakeService.getSnakeScoreList();

        return ApiResponse.success(snakeScoreList);
    }
}
