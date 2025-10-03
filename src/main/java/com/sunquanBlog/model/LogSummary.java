package com.sunquanBlog.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogSummary {
    private Integer id;
    private String ip;
    private String page;
    private String city;
    private LocalDateTime createTime;
    private String browser;
    private String userAction;
    private String platform;
    private String userId;
    private LocalDateTime entryTime;
    private LocalDateTime leaveTime;
    private LocalDate visitDay;
    private Integer pv;
    private Long register;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDate getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(LocalDate visitDay) {
        this.visitDay = visitDay;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Long getRegister() {
        return register;
    }

    public void setRegister(Long register) {
        this.register = register;
    }
}
