package com.sunquanBlog.service;

import com.sunquanBlog.model.TimeLine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimeLineService {
    List<TimeLine> getAllTimeLine();

    int deleteTimeLine(Integer id);
}
