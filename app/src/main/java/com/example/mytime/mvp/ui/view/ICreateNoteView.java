package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreateNoteView {

    /**
     * 编辑状态下展示数据
     */
    void showData(Note note, List<Photo> list);

    void complete();
}
