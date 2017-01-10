package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.presenter.IAllPlanPresenter;
import com.example.mytime.mvp.presenter.impl.AllPlanPresenterImpl;
import com.example.mytime.mvp.ui.adapter.AllNoteAdapter;
import com.example.mytime.mvp.ui.adapter.AllPlanAdapter;
import com.example.mytime.mvp.ui.view.IAllPlanView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllPlanActivity extends AppCompatActivity implements IAllPlanView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private IAllPlanPresenter allPlanPresenter;
    private boolean isCompletePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plan);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        isCompletePlan = intent.getBooleanExtra("COMPLETEPLAN", false);
        allPlanPresenter = new AllPlanPresenterImpl( this);
        if ( !isCompletePlan){
            allPlanPresenter.showAllPlan( true);
        }else {
            allPlanPresenter.showAllCompletePlan( true);
        }


    }

    @Override
    public void showAllPlan(List<Plan> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager);
        AllPlanAdapter allPlanAdapter = new AllPlanAdapter(this, list);
        recyclerView.setAdapter( allPlanAdapter);
    }

    @Override
    public void showAllComplete(List<Plan> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager);
        AllPlanAdapter allPlanAdapter = new AllPlanAdapter(this, list);
        recyclerView.setAdapter( allPlanAdapter);
    }
}
