package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Snake;
import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.SnakeService;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/createScore")
    public ApiResponse createScore(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
        Snake snake = new Snake();
        
        snake.setScore((Integer) requestBody.get("score"));
        snake.setGameTime((Integer) requestBody.get("gameTime"));

        snake.setIp(logService.getClientIpAddress(request));
        snake.setCity(logService.getLocation(request));
        snake.setUserId(requestBody.get("userId") == null ? 0 : (Integer) requestBody.get("userId"));

        Integer response = snakeService.createScore(snake);
        if (response == 1) {
            return ApiResponse.success("创建成功");
        } else {
            return ApiResponse.error(500, "创建失败");
        }
    }
}
