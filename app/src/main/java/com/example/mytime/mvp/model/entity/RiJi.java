package com.example.mytime.mvp.model.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/5/28.
 */

public class RiJi extends DataSupport implements Serializable {
    int id;
    String content;
    long createTime;
    String weather;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
