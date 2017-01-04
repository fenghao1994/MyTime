package com.example.mytime.mvp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.ui.activity.CreateNoteActivity;
import com.example.mytime.mvp.ui.adapter.NoteAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {


    private static final int REQUEST_NOTE = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private NoteAdapter adapter;

    public List<Note> notes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager( layoutManager);
        return view;
    }
    public void init(){
        notes = DataSupport.findAll(Note.class);
        adapter = new NoteAdapter( notes);
        recyclerView.setAdapter( adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                createNote();
                break;
        }
    }
    private void createNote(){
        Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
        startActivityForResult( intent, REQUEST_NOTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK){
            if ( requestCode == REQUEST_NOTE){
                init();
            }
        }
    }
}
