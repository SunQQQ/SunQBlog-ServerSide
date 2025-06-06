package com.sunquanBlog.model;

import java.time.LocalDateTime;

public class Log {
    private Integer id;
    private String ip;
    private String page;
    private String ipCity;
    private LocalDateTime createTime;
    private String browser;
    private String action;
    private String actionObject;

    private String actionDesc;

    private String platformType;

    private Long todayIpCount;

    private Long totalIpCount;

    private Long todayPvCount;

    private Long totalPvCount;
    public Log() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getIpCity() {
        return ipCity;
    }

    public void setIpCity(String ipCity) {
        this.ipCity = ipCity;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionObject() {
        return actionObject;
    }

    public void setActionObject(String actionObject) {
        this.actionObject = actionObject;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Long getTodayIpCount() {
        return todayIpCount;
    }

    public void setTodayIpCount(Long todayIpCount) {
        this.todayIpCount = todayIpCount;
    }

    public Long getTotalIpCount(){
        return totalIpCount;
    }

    public void setTotalIpCount(Long totalIpCount) {
        this.totalIpCount = totalIpCount;
    }

    public Long getTodayPvCount() {
        return todayPvCount;
    }

    public void setTodayPvCount(Long todayPvCount) {
        this.todayPvCount = todayPvCount;
    }

    public Long getTotalPvCount() {
        return totalPvCount;
    }

    public void setTotalPvCount(Long totalPvCount) {
        this.totalPvCount = totalPvCount;
    }
}
