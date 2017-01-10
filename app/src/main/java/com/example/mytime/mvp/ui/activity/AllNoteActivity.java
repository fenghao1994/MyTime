package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.presenter.IAllNotePresenter;
import com.example.mytime.mvp.presenter.impl.AllNotePresenterImpl;
import com.example.mytime.mvp.ui.adapter.AllNoteAdapter;
import com.example.mytime.mvp.ui.view.IAllNoteView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNoteActivity extends AppCompatActivity implements IAllNoteView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    IAllNotePresenter allNotePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_note);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        allNotePresenter = new AllNotePresenterImpl( this);
        allNotePresenter.showAllNotes( true);
    }

    @Override
    public void showAllNote(List<Note> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager);
        AllNoteAdapter allNoteAdapter = new AllNoteAdapter(this, list);
        recyclerView.setAdapter( allNoteAdapter);
    }
}
