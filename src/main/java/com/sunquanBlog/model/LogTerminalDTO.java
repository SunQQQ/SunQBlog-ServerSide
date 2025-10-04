package com.sunquanBlog.model;

public class LogTerminalDTO {
    private Long pcNum; // PC端访问量
    private Long mobileNum; // 移动端访问量
    private Long bothNum; // 既有PC端又有移动端访问量

    public Long getBothNum() {
        return bothNum;
    }

    public void setBothNum(Long bothNum) {
        this.bothNum = bothNum;
    }

    public LogTerminalDTO() {
    }

    public Long getPcNum() {
        return pcNum;
    }

    public void setPcNum(Long pcNum) {
        this.pcNum = pcNum;
    }

    public Long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Long mobileNum) {
        this.mobileNum = mobileNum;
    }
}
