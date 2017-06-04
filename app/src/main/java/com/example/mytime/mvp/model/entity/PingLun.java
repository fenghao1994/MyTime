package com.example.mytime.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/6/4.
 */

public class PingLun implements Serializable {
    private String content;
    private String phoneNumber;
    private long createTime; //planitem的创立时间
    private long editTime; //评论时间
    private String deletePinglun; //是否被删除

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    public String getDeletePinglun() {
        return deletePinglun;
    }

    public void setDeletePinglun(String deletePinglun) {
        this.deletePinglun = deletePinglun;
    }
}
