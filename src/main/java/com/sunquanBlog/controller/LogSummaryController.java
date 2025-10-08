package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.job.LogSummaryJob;
import com.sunquanBlog.service.LogService;
import com.sunquanBlog.service.LogSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LogSummaryController {
    @Autowired
    private LogSummaryService logSummaryService;
    @Autowired
    private LogSummaryJob logSummaryJob;
    @Autowired
    private LogService log;
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

    /**
     * 从log表汇集数据到log_summary表
     * @return
     */
    @PostMapping("/backfillHistoryData")
    public ApiResponse backfillHistoryData(){
        return ApiResponse.success(logSummaryJob.backfillHistoryData());
    }

    /**
     * 获取当日和累计访问的ip/pv量
     * @return
     */
    @PostMapping("/getLogIp")
    public ApiResponse getTimeLineList(HttpServletRequest request) {
        // 记录打开访问统计页日志
        log.createLog(request,"用户端","访问统计","打开","访问统计页","");

        return logSummaryService.getLogIp();
    }

    /**
     * 折线图接口
     */
    @PostMapping("/ip-daily")
    public ApiResponse getIpDaily(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getIpDaily(days,request);
    }

    /**
     * 地图接口
     * @param requestBody
     * @return
     */
    @PostMapping("/city-daily")
    public ApiResponse getCityDaily(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getCityDaily(days,request);
    }

    /**
     * 获取终端类型占比
     * @return
     */
    @PostMapping("/getTerminal")
    public ApiResponse getTerminal(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getTerminal(days,request);
    }

    @PostMapping("/getUserAction")
    public ApiResponse getUserAction(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer day  = (Integer) requestBody.get("day");
        return logSummaryService.getUserAction(day,request);
    }

    @PostMapping("/getPageDaily")
    public ApiResponse getPageDaily(@RequestBody Map<String,Object> requestBody,HttpServletRequest request) {
        // 距离当天的相差天数
        Integer days  = (Integer) requestBody.get("days");
        return logSummaryService.getPageDaily(days);
    }

    /**
     * 获取最新的汇总数据的截止时间
     * @return
     */
    @PostMapping("/getLatestCutoffTime")
    public ApiResponse getLatestCutoffTime() {
        return logSummaryService.getLatestCutoffTime();
    }
}
