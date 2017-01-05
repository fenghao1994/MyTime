package com.example.mytime.mvp.model.impl;

import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.ICreateNoteEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreateNoteEntityImpl implements ICreateNoteEntity {

    /**
     * 临时note  获取存入note的id
     */
    private Note currentNote;

    @Override
    public void saveNote(Note note, List<Photo> list) {
        note.save();
        currentNote = DataSupport.where("createTime = ?", note.getCreateTime() + "").findFirst(Note.class);
        if ( list != null && list.size() > 0){
            for (int i = 0 ; i < list.size(); i++){
                list.get(i).setObjectType(2);
                list.get(i).setObjectId( currentNote.getId());
                list.get(i).save();
            }
        }
    }

    @Override
    public boolean updateNote(Note note, List<Photo> list) {

        note.update( note.getId());
//        currentNote = DataSupport.where("createTime = ?", note.getCreateTime() + "").findFirst(Note.class);
        if ( list != null){
            DataSupport.deleteAll(Photo.class, "objectType = ? and objectId = ?", 2 + "", note.getId() + "");
            for (int i = 0 ; i < list.size(); i++){
                list.get(i).setObjectType(2);
                list.get(i).setObjectId( note.getId());
                list.get(i).save();
            }
        }

        return false;
    }

    @Override
    public List<Photo> getPhotoAddress(Note note) {

        return DataSupport.where("objectId = ? and objectType = ?", note.getId() + "", 2 + "").find(Photo.class);
    }


}
