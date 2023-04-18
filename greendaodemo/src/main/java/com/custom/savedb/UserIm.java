package com.custom.savedb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserIm {
    @Id
    private Long id;
    private String userId;
    private String name;
    private String  url;
    @Generated(hash = 920605169)
    public UserIm(Long id, String userId, String name, String url) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.url = url;
    }
    @Generated(hash = 1638259976)
    public UserIm() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
