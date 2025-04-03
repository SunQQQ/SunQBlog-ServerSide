package com.sunquanBlog.model;

import java.time.LocalDateTime;

public class LogDTO { // DTO类，用于封装查询结果
    private String ip;
    private String actions;          // GROUP_CONCAT的结果
    private String ipCity;           // MAX(ip_city)
    private String browser;          // MAX(browser)
    private LocalDateTime leaveTime; // MAX(create_time)
    private LocalDateTime entryTime; // MIN(create_time)

    // 构造方法、getter/setter 省略（可用Lombok @Data注解简化）
    public LogDTO() {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getIpCity() {
        return ipCity;
    }

    public void setIpCity(String ipCity) {
        this.ipCity = ipCity;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}