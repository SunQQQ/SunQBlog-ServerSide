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
import java.util.HashMap;
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

    @PostMapping("/getSnakeScoreTopList")
    public ApiResponse getSnakeScoreTopList() {
        List<Snake> snakeScoreList = snakeService.getSnakeScoreTopList(5);
        Map<String, Object> map = new HashMap<>();
        map.put("list", snakeScoreList);
        map.put("total",snakeService.getScoreListCount());

        return ApiResponse.success(map);
    }

    @PostMapping("/createScore")
    public ApiResponse createScore(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
        Snake snake = new Snake();

        snake.setScore((Integer) requestBody.get("score"));
        snake.setGameTime((Integer) requestBody.get("gameTime"));
        snake.setUserName((String) requestBody.get("userName"));

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

    @PostMapping("/scoreMulDelete")
    public ApiResponse scoreMulDelete(@RequestBody Map<String, Object> requestBody) {
        List ids = (List) requestBody.get("ids");
        Integer response = snakeService.scoreMulDelete(ids);
        if (response > 0) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error(500, "删除失败");
        }
    }
}
