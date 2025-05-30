package com.sunquanBlog.service.impl;

import com.sunquanBlog.model.TimeLine;
import com.sunquanBlog.service.TimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunquanBlog.mapper.TimeLineMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Override
    public int insertTimeLine(TimeLine timeLine){
        return timeLineMapper.insertTimeLine(timeLine.getContent(), timeLine.getContentDate());
    }

    @Override
    public int updateTimeLine(TimeLine timeLine) {
        return timeLineMapper.updateTimeLine(timeLine.getId(), timeLine.getContent(), timeLine.getContentDate());
    }
}
