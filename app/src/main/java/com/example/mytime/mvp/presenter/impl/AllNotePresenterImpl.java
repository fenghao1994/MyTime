package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreateNoteEntity;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.impl.CreateNoteEntityImpl;
import com.example.mytime.mvp.presenter.IAllNotePresenter;
import com.example.mytime.mvp.ui.view.IAllNoteView;

import java.util.List;

/**
 * Created by fenghao on 2017/1/10.
 */

public class AllNotePresenterImpl implements IAllNotePresenter {

    IAllNoteView allNoteView;
    ICreateNoteEntity createNoteEntity;

    public AllNotePresenterImpl(IAllNoteView allNoteView) {
        this.allNoteView = allNoteView;
        createNoteEntity = new CreateNoteEntityImpl();
    }

    @Override
    public void showAllNotes(boolean desc) {
        List<Note> list = createNoteEntity.getNotes( desc);
        allNoteView.showAllNote( list);
    }
}
