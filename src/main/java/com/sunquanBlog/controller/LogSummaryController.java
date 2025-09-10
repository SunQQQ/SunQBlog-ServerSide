package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.job.LogSummaryJob;
import com.sunquanBlog.service.LogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogSummaryController {
    @Autowired
    private LogSummaryService logSummaryService;
    @Autowired
    private LogSummaryJob logSummaryJob;

    /**
     * 某段时间内老用户访问比例
     * @param requestBody
     * @return
     */
    @PostMapping("/old-users")
    public ApiResponse getOldUser(@RequestBody Map<String,Object> requestBody){
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getOldUser(days);
    }

    /**
     * 平台访问比例
     * @param requestBody
     * @return
     */
    @PostMapping("/platform-ratio")
    public ApiResponse platformRatio(@RequestBody Map<String,Object> requestBody){
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getPlatFormRatio(days);
    }

    @PostMapping("/backfillHistoryData")
    public ApiResponse backfillHistoryData(){
        return ApiResponse.success(logSummaryJob.backfillHistoryData());
    }
}
