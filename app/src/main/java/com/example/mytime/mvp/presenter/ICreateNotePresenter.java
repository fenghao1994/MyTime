package com.example.mytime.mvp.presenter;

import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreateNotePresenter {


    void saveNote(Note note, List<Photo> photos);


    /**
     * 编辑状态下展示数据
     */
    void showData(Note note);

    /**
     * 更新数据
     */
    void update(Note note, List<Photo> photos);

    /**
     * 网络情况下
     * 本地获得存储后的note
     * @param createTime
     * @return
     */
    Note getNote(long createTime);

    List<Photo> getPhoto(Note note);
}
