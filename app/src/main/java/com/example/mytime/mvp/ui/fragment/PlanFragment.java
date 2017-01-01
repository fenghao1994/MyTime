package com.example.mytime.mvp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.activity.CreatePlan;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 计划fragment
 */
public class PlanFragment extends Fragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, view);
        return view;
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
        Intent intent = new Intent(getActivity(), CreatePlan.class);
        startActivity( intent);
    }

}
