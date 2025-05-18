package com.sunquanBlog.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeLine {
    private int id;
    private String content;
    private LocalDate contentDate;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public TimeLine() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getContent_date() {
        return contentDate;
    }

    public void setContent_date(LocalDate contentDate) {
        this.contentDate = contentDate;
    }

    public LocalDateTime getCreate_time() {
        return createTime;
    }

    public void setCreate_time(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

