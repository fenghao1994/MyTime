package com.example.mytime.mvp.model.entity;

import java.util.ArrayList;

/**
 * Created by fenghao on 2017/1/4.
 * 计划表子项item
 */

public class PlanItem {
    int id;
    /**
     * 计划表的唯一值
     */
    String planId;
    String title;
    String content;
    String createTime;
    String editTime;
    boolean isEdit;
    String phoneNumber;
    String messageContent;
    String messagePhoneNumber;
    String location;
    String time;
    boolean isEveryDay;
    boolean isManyDays;
    boolean isExpired;
    boolean isComplete;

    ArrayList<Photo> address;

    public ArrayList<Photo> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Photo> address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessagePhoneNumber() {
        return messagePhoneNumber;
    }

    public void setMessagePhoneNumber(String messagePhoneNumber) {
        this.messagePhoneNumber = messagePhoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isEveryDay() {
        return isEveryDay;
    }

    public void setEveryDay(boolean everyDay) {
        isEveryDay = everyDay;
    }

    public boolean isManyDays() {
        return isManyDays;
    }

    public void setManyDays(boolean manyDays) {
        isManyDays = manyDays;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
