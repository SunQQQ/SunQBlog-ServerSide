package com.sunquanBlog.model;

import java.math.BigInteger;

public class UserName {
    private Integer id;
    private String name;
    private BigInteger isUsed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(BigInteger isUsed) {
        this.isUsed = isUsed;
    }
}
