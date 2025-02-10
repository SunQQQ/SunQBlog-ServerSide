package com.sunquanBlog.model;

import java.time.LocalDateTime;


public class LeaveMessage {
    private Integer id;
    private String leave_name;
    private Integer leave_id;
    private String message_content;
    private String city;
    private String avatar;
    private LocalDateTime create_time;
    private LocalDateTime model_time;
    private LocalDateTime parent_id;

    public LeaveMessage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getLeave_person() {
        return leave_name;
    }

    public void setLeave_person(String leave_name) {
        this.leave_name = leave_name;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public LocalDateTime getModel_time() {
        return model_time;
    }

    public void setModel_time(LocalDateTime model_time) {
        this.model_time = model_time;
    }

    public LocalDateTime getParent_id() {
        return parent_id;
    }

    public void setParent_id(LocalDateTime parent_id) {
        this.parent_id = parent_id;
    }

    public Integer getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(Integer leave_id) {
        this.leave_id = leave_id;
    }
}

