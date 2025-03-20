package com.sunquanBlog.model;

import java.time.LocalDateTime;
import java.util.List;


public class LeaveMessage {
    private Integer id;
    private String leaveName;
    private Integer leaveId;
    private String messageContent;
    private String city;
    private LocalDateTime createTime;
    private LocalDateTime modelTime;
    private Integer parentId;
    private List<LeaveMessage> child;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public List<LeaveMessage> getChild() {
        return child;
    }

    public void setChild(List<LeaveMessage> child) {
        this.child = child;
    }
}

