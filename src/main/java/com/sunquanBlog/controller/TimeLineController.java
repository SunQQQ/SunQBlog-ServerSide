package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeLineController {
    @Autowired
    private TimeLineService timeLineService;

    @PostMapping("/getTimeLineList")
    public ApiResponse getTimeLineList() {
        List<TimeLine> timeLineList = timeLineService.getAllTimeLine();
        return ApiResponse.success(timeLineList);
    }
}
