package com.example.mytime.mvp.model.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fenghao on 2017/1/4.
 * 计划表子项item
 */

public class PlanItem extends DataSupport implements Serializable{
    int id;
    /**
     * 计划表的唯一值
     */
    long planId;
    String title;
    String content;
    long createTime;
    long editTime;
    boolean isEdit;
    String phoneNumber;
    String messageContent;
    String messagePhoneNumber;
    String location;
    boolean isEveryDay;
    boolean isManyDays;
    boolean isExpired;
    boolean isComplete;

    ArrayList<Photo> address;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int alarmWay;//0为默认 只提醒一次，1为每日，2为每周，3为每月

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    public int getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(int alarmWay) {
        this.alarmWay = alarmWay;
    }

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

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
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
