package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.HeartFeltMapper;
import com.sunquanBlog.model.HeartFelt;
import com.sunquanBlog.service.HeartFeltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeartFeltServiceImpl implements HeartFeltService {
    @Autowired
    private HeartFeltMapper heartFeltMapper;
    @Override
    public ApiResponse editHeartFelt(Integer id,String content,String writer,String creater) {
        int updateNum = heartFeltMapper.updateHeartFelt(id,content,writer,creater);
        if(updateNum == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }

    @Override
    public ApiResponse createHeartFelt(String content,String writer,Integer creater) {
        int createNum = heartFeltMapper.createHeartFelt(content,writer,creater);
        if(createNum == 1){
            return ApiResponse.success("创建成功");
        }else {
            return ApiResponse.error(500,"创建失败，请留言");
        }
    }

    @Override
    public ApiResponse deleteHeartFelt(int id) {
        if(heartFeltMapper.deleteHeart(id) == 1){
            return ApiResponse.success("删除成功");
        }else {
            return ApiResponse.error(500,"删除失败");
        }
    }

    @Override
    public ApiResponse getHeartFeltList(Integer id) {
        List<HeartFelt> heartFeltList = heartFeltMapper.getAllHeartFelt(id);
        return ApiResponse.success(heartFeltList);
    }
}
