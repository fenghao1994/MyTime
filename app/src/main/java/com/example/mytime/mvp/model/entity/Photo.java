package com.example.mytime.mvp.model.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/1/4.
 * 图片地址实体
 */

public class Photo extends DataSupport implements Serializable{
    int id;
    int objectType;
    int objectId;
    String address;
    long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
