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
import com.example.mytime.mvp.ui.activity.CreateNoteActivity;
import com.example.mytime.mvp.ui.adapter.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager( layoutManager);
        adapter = new NoteAdapter( mList);
        recyclerView.setAdapter( adapter);

        return view;
    }
    //// TODO: 2017/1/1  测试
    private List mList = new ArrayList();
    public void init(){
        for (int i = 0 ;i < 30; i++){
            mList.add("便签" + i);
        }
    }
    @OnClick({R.id.recycler_view, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recycler_view:
                break;
            case R.id.fab:
                createNote();
                break;
        }
    }
    private void createNote(){
        Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
        startActivity( intent);
    }
}
