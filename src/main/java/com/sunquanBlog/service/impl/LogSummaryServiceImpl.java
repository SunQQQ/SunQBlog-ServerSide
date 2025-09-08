package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LogSummaryMapper;
import com.sunquanBlog.service.LogSummaryService;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class LogSummaryServiceImpl implements LogSummaryService {
    @Autowired
    LogSummaryMapper logSummaryMapper;
    @Override
    public ApiResponse<Map> getOldUser(Integer days){
        Integer oldUser = 0;
        Integer newUser = 0;
        List<Map> ipData;

        if(days == 0){
            ipData = logSummaryMapper.getTodayOldUser(days);
        }else {
            ipData = logSummaryMapper.getOldUser(days);
        }

        for (Map user : ipData) {
            String type = (String) user.get("user_type");
            if (type.equals("新用户")) {
                newUser++;
            } else {
                oldUser++;
            }
        }

        Map<String,Number> result = new HashMap<>();
        result.put("oldUser", oldUser);
        result.put("newUser", newUser);
//        result.put("total", oldUser + newUser);

        return ApiResponse.success(result);
    }
}
