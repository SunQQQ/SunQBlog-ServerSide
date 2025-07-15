package com.sunquanBlog.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FriendUrl{
    private Integer id;
    private String siteName;
    private String siteUrl;
    private String siteDesc;
    private String siteLogo;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public FriendUrl() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
    }

    public String getSiteLogo() {
        return siteLogo;
    }

    public void setSiteLogo(String siteLogo) {
        this.siteLogo = siteLogo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
