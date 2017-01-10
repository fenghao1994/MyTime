package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Note;

import java.util.List;

/**
 * Created by fenghao on 2017/1/10.
 */

public interface IAllNoteView {
    /**
     * 展示所有的note
     * @param list
     */
    void showAllNote(List<Note> list);
}
