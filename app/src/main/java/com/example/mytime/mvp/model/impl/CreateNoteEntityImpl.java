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
                list.get(i).setCreateTime(currentNote.getCreateTime());
                list.get(i).save();
            }
        }
    }

    @Override
    public boolean updateNote(Note note, List<Photo> list) {

        note.update( note.getId());
//        currentNote = DataSupport.where("createTime = ?", note.getCreateTime() + "").findFirst(Note.class);
        if ( list != null){
            DataSupport.deleteAll(Photo.class, "objectType = ? and createTime = ?", 2 + "", note.getCreateTime() + "");
            for (int i = 0 ; i < list.size(); i++){
                Photo photo = new Photo();
                photo.setObjectId(note.getId());
                photo.setObjectType(2);
                photo.setAddress(list.get(i).getAddress());
                photo.setCreateTime(note.getCreateTime());
                photo.save();
            }
        }

        return false;
    }

    @Override
    public List<Photo> getPhotoAddress(Note note) {

        return DataSupport.where("createTime = ? and objectType = ?", note.getCreateTime() + "", 2 + "").find(Photo.class);
    }

    @Override
    public List<Note> getNotes(boolean desc) {
        if ( desc){
            return DataSupport.order("createTime desc").find( Note.class);
        }
        return DataSupport.findAll(Note.class);
    }

    @Override
    public Note getNote(long createTime) {
        List<Note> notes = DataSupport.where("createTime = ?", createTime + "").find(Note.class);
        if (notes != null){
            return notes.get(0);
        }
        return null;
    }


}
