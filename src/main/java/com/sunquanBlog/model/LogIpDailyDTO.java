package com.sunquanBlog.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogIpDailyDTO { // DTO类，用于封装查询结果
    private Long ip;
    private Long pv;          // GROUP_CONCAT的结果

    private Long register;     // 城市名称

    @JsonFormat(pattern = "YYYYMMdd")
    private LocalDate day;    // 平台类型

    public LogIpDailyDTO() {
    }


    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public Long getPv() {
        return pv;
    }

    public void setPv(Long pv) {
        this.pv = pv;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Long getRegister() {
        return register;
    }

    public void setRegister(Long register) {
        this.register = register;
    }
}