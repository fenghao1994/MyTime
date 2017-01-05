package com.example.mytime.mvp.model;

import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 * 创建note和编辑note
 */

public interface ICreateNoteEntity {
    /**
     * 连着photo一起存储
     * @param note
     * @param list
     * @return
     */
    void saveNote(Note note, List<Photo> list);

    /**
     * 连着photo一起修改
     * @param note
     * @param list
     * @return
     */
    boolean updateNote(Note note, List<Photo> list);

    List<Photo> getPhotoAddress(Note note);

}
