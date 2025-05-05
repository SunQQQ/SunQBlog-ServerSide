package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TimeLineController {
    @Autowired
    private TimeLineService timeLineService;
    @Autowired
    private LogService logService;

    @PostMapping("/getTimeLineList")
    public ApiResponse getTimeLineList(HttpServletRequest request) {
        List<TimeLine> timeLineList = timeLineService.getAllTimeLine();

        // 记录日志
        logService.createLog(request, "用户端","时间轴", "打开", "时间轴","",0);

        return ApiResponse.success(timeLineList);
    }
}
