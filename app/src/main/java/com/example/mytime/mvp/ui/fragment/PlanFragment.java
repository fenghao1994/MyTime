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
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.ui.activity.CreatePlanActivity;
import com.example.mytime.mvp.ui.adapter.PlanAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * 计划fragment
 */
public class PlanFragment extends Fragment {


    private static final int REQUEST_PLAN = 1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private PlanAdapter adapter;

    private List<Plan> mList;
    
    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, view);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager( layoutManager);

        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init(){
        mList = DataSupport.findAll(Plan.class);
        adapter = new PlanAdapter( mList);
        recyclerView.setAdapter( adapter);
    }

    @OnClick({R.id.recycler_view, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recycler_view:
                break;
            case R.id.fab:
                createPlan();
                break;
        }
    }

    private void createPlan(){
        Intent intent = new Intent(getActivity(), CreatePlanActivity.class);
        startActivityForResult( intent, REQUEST_PLAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_PLAN){
                init();
            }
        }
    }
}
