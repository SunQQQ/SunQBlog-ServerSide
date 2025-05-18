package com.sunquanBlog.service.impl;

import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunquanBlog.mapper.TimeLineMapper;

import java.util.List;

@Service
public class TimeLineServiceImpl implements TimeLineService {
    @Autowired
    private TimeLineMapper timeLineMapper;

    @Override
    public List<TimeLine> getAllTimeLine() {
        return timeLineMapper.getAllTimeLine();
    }

    @Override
    public int deleteTimeLine(Integer id){
        return timeLineMapper.deleteTimeLine(id);
    }
}
