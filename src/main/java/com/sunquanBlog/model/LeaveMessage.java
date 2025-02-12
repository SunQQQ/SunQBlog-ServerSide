package com.sunquanBlog.model;

import java.time.LocalDateTime;


public class LeaveMessage {
    private Integer id;
    private String leaveName;
    private Integer leaveId;
    private String messageContent;
    private String city;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime modelTime;
    private LocalDateTime parentId;

    public LeaveMessage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModelTime() {
        return modelTime;
    }

    public void setModelTime(LocalDateTime modelTime) {
        this.modelTime = modelTime;
    }

    public LocalDateTime getParentId() {
        return parentId;
    }

    public void setParentId(LocalDateTime parentId) {
        this.parentId = parentId;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }
}

