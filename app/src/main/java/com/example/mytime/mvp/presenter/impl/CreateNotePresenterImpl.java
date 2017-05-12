package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreateNoteEntity;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.impl.CreateNoteEntityImpl;
import com.example.mytime.mvp.presenter.ICreateNotePresenter;
import com.example.mytime.mvp.ui.view.ICreateNoteView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreateNotePresenterImpl implements ICreateNotePresenter {

    private ICreateNoteView createNoteView;
    private ICreateNoteEntity createNoteEntity;


    public CreateNotePresenterImpl(ICreateNoteView iCreateNoteView){
        this.createNoteView = iCreateNoteView;
        createNoteEntity = new CreateNoteEntityImpl();
    }

    @Override
    public void saveNote(Note note, List<Photo> photos) {
        createNoteEntity.saveNote( note, photos);
        createNoteView.complete();
    }

    @Override
    public void showData(Note note) {
        ArrayList<Photo> arrayList = (ArrayList<Photo>) createNoteEntity.getPhotoAddress( note);
        createNoteView.showData( note, arrayList);
    }

    @Override
    public void update(Note note, List<Photo> photos) {
        createNoteEntity.updateNote(note, photos);
        createNoteView.complete();
    }

    @Override
    public Note getNote(long createTime) {
        return createNoteEntity.getNote(createTime);
    }

    @Override
    public List<Photo> getPhoto(Note note) {
        ArrayList<Photo> arrayList = (ArrayList<Photo>) createNoteEntity.getPhotoAddress( note);
        return arrayList;
    }
}
