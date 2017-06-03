package com.example.mytime.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/6/3.
 */

public class Friend implements Serializable {
    User user;
    String userActive;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserActive() {
        return userActive;
    }

    public void setUserActive(String userActive) {
        this.userActive = userActive;
    }
}
