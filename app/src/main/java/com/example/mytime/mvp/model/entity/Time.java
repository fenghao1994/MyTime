package com.example.mytime.mvp.model.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by fenghao on 2017/1/4.
 */

public class Time extends DataSupport implements Serializable {
    int id;
    int planItemId;
    long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanItemId() {
        return planItemId;
    }

    public void setPlanItemId(int planItemId) {
        this.planItemId = planItemId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
