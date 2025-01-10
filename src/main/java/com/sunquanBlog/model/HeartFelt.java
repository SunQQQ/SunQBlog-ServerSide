package com.sunquanBlog.model;

import java.time.LocalDateTime;


public class HeartFelt {
    private Integer id;
    private String content;
    private String writer;
    private String creater_id;
    private LocalDateTime create_time;

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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreater() {
        return creater_id;
    }

    public void setCreater(String creater_id) {
        this.creater_id = creater_id;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }
}
