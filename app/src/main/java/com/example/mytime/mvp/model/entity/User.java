package com.example.mytime.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/5/7.
 */

public class User implements Serializable{
    public String phoneNumber;
    public String password;
    public String headImg;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
