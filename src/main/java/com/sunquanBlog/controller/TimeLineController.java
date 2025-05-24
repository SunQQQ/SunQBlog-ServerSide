package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.SysLoginService;
import com.sunquanBlog.service.TimeLineService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class TimeLineController {
    @Autowired
    private TimeLineService timeLineService;
    @Autowired
    private LogService logService;
    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("/getTimeLineList")
    public ApiResponse getTimeLineList(HttpServletRequest request) {
        List<TimeLine> timeLineList = timeLineService.getAllTimeLine();

        // 记录日志
        logService.createLog(request, "用户端","时间轴", "打开", "时间轴","",0);

        return ApiResponse.success(timeLineList);
    }

    @PostMapping("/deleteTimeLine")
    public ApiResponse deleteTimeLine(HttpServletRequest request, @RequestBody Map<String,Integer> requestBody) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);
        String role = sysLoginService.getUserById(userId).getRole();

        if(!role.equals("master")) {
            return ApiResponse.error(500,"没有权限");
        }

        int result = timeLineService.deleteTimeLine(requestBody.get("id"));

        if (result > 0) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error(500,"删除失败");
        }
    }

    @PostMapping("/insertTimeLine")
    public ApiResponse insertTimeLine(HttpServletRequest request, @RequestBody TimeLine timeline) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);
        String role = sysLoginService.getUserById(userId).getRole();

        if(!role.equals("master")) {
            return ApiResponse.error(500,"权限不足");
        }

        int result = timeLineService.insertTimeLine(timeline);

        if (result > 0) {
            return ApiResponse.success("添加成功");
        } else {
            return ApiResponse.error(500,"添加失败");
        }
    }
}
