package com.example.mytime.mvp.model.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fenghao on 2017/1/4.
 * 便签实体
 */

public class Note extends DataSupport implements Serializable{

    int id;
    String title;
    String content;
    long createTime;
    long editTime;
    boolean isEdit;
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
}
