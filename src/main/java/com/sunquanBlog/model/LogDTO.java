package com.sunquanBlog.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class LogDTO { // DTO类，用于封装查询结果
    private String ip;
    private String actions;          // GROUP_CONCAT的结果
    private String ipCity;           // MAX(ip_city)
    private String browser;          // MAX(browser)
    private String userName;

    private String fromUrl;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime leaveTime; // MAX(create_time)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime entryTime; // MIN(create_time)

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime day;    // 平台类型

    private boolean isCurUser;

    private Long stayTime;

    private String formatStayTime;

    // 构造方法、getter/setter 省略（可用Lombok @Data注解简化）
    public LogDTO() {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getStayTime() {
        return stayTime;
    }

    public void setStayTime(Long stayTime) {
        this.stayTime = stayTime;
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

    public boolean getIsCurUser() {
        return isCurUser;
    }

    public void setIsCurUser(boolean curUser) {
        isCurUser = curUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getFormatStayTime() {
        return formatStayTime;
    }

    public void setFormatStayTime(String formatStayTime) {
        this.formatStayTime = formatStayTime;
    }
}