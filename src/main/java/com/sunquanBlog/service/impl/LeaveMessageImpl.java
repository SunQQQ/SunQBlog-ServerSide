package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.LeaveMessageMapper;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.LeaveMessage;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.CityCodeConverter;
import com.sunquanBlog.service.LeaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunquanBlog.service.LogService;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveMessageImpl implements LeaveMessageService {
    @Autowired
    private LeaveMessageMapper leaveMessageMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private CityCodeConverter cityCodeConverter;
    @Autowired
    private WebClient webClient; // 注入WebClient
    @Override
    public ApiResponse updateLeaveMessage(Map<String,Object> map) {
        int updateNum = leaveMessageMapper.updateLeaveMessage(map);
        if(updateNum == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }

    @Override
    public ApiResponse updateLeaveMessage(Integer id,String messageContent,String city,String avator,Integer parentId,String leaveName) {
        int updateNum = leaveMessageMapper.updateLeaveMessage(id,messageContent,city,avator,parentId,leaveName);
        if(updateNum == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }

    @Override
    public ApiResponse createLeaveMessage(Map<String,Object> map, Integer accountId, HttpServletRequest request) {
        // 留言时需为已登录状态，故无需再填入留言人信息，直接从token中id获取name即可
        String userName = loginMapper.getUserById(accountId).getName();

        String city = logService.getLocation(request);
        map.put("city", city);

        int createNum = leaveMessageMapper.createLeaveMessage(map,accountId,userName);
        if(createNum == 1){
            // 记录日志
            logService.createLog(request,"用户端", "留言页", "创建" , "留言", "："+map.get("messageContent"),"");
            return ApiResponse.success("创建成功");
        }else {
            return ApiResponse.error(500,"创建失败，请留言");
        }
    }

    @Override
    public ApiResponse deleteLeaveMessage(int id) {
        if(leaveMessageMapper.deleteLeaveMessage(id) == 1){
            return ApiResponse.success("删除成功");
        }else {
            return ApiResponse.error(500,"删除失败");
        }
    }

    @Override
    public ApiResponse getAllLeaveMessage(Integer id,Integer start,Integer size) {
        User user = loginMapper.getUserById(id);
        String role = user.getRole();
        List<LeaveMessage> heartFeltList = leaveMessageMapper.getAllLeaveMessage(id,role,start,size);

        Map<String, Object> map = new HashMap<>();
        map.put("list",heartFeltList);
        map.put("total",leaveMessageMapper.getCountByRole(id,role));

        return ApiResponse.success(map);
    }

    // 此方法用于用户端留言列表
    @Override
    public ApiResponse getAllLeaveMessage(Integer start,Integer size) {
        // 先查分页范围内的一级数据
        List<LeaveMessage> level1List = leaveMessageMapper.getuserSideLevel1(start,size);

        // 取出一级数据的id
        List<Integer> parentIdArray = new ArrayList<>();
        for (int i = 0; i < level1List.size(); i++) {
            LeaveMessage level1Item = level1List.get(i);
            Integer id = level1Item.getId(); // 获取 id
            parentIdArray.add(id);
        }

        // 再查以上一级数据的二级数据
        List<LeaveMessage> level2List = leaveMessageMapper.getuserSideLevel2(parentIdArray);

        // 组装如上两级数据
        for (int i = 0; i < level1List.size(); i++) {
            LeaveMessage level1Item = level1List.get(i);
            List<LeaveMessage> childList = new ArrayList<>();
            for (int j = 0; j < level2List.size(); j++) {
                LeaveMessage child = level2List.get(j);
                if (level1Item.getId().equals(child.getParentId())) {
                    childList.add(child);
                }
            }
            level1Item.setChild(childList);
        }

        return ApiResponse.success(level1List);
    }

    @Override
    public ApiResponse getLmCount() {
        int count = leaveMessageMapper.getLmCount();
        return ApiResponse.success(count);
    }

    @Override
    public ApiResponse getWeather(HttpServletRequest request) {
        String city = logService.getLocation(request);
        System.out.print("城市名称："+city+"\n");
        String cityCode = cityCodeConverter.getCityCode(city);
        System.out.print("城市代码："+cityCode+"\n");

        if (cityCode == null) {
            return ApiResponse.error(500, "城市代码未找到");
        }

        // 发起异步请求
        Map<String, Object> weatherData = webClient.get()
                .uri("https://restapi.amap.com/v3/weather/weatherInfo?key=ba5f9b69f0541123a4dbe142da230b4d&city={city}&extensions=all&output=JSON", cityCode)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // 阻塞等待结果（如需异步可返回Mono）

        return ApiResponse.success(weatherData);
    }
}
