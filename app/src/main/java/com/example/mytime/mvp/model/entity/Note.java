package com.example.mytime.mvp.model.entity;

import java.util.ArrayList;

/**
 * Created by fenghao on 2017/1/4.
 * 便签实体
 */

public class Note {
    int id;
    String title;
    String content;
    String createTime;
    String editTime;
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
}
