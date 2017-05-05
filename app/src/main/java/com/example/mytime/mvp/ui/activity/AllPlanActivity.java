package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.presenter.IAllPlanPresenter;
import com.example.mytime.mvp.presenter.impl.AllPlanPresenterImpl;
import com.example.mytime.mvp.ui.adapter.AllNoteAdapter;
import com.example.mytime.mvp.ui.adapter.AllPlanAdapter;
import com.example.mytime.mvp.ui.adapter.PlanAdapter;
import com.example.mytime.mvp.ui.adapter.PlanItemAdapter;
import com.example.mytime.mvp.ui.view.IAllPlanView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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

        setSupportActionBar(toolbar);

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
//        showData(list);
    }

    @Override
    public void showAllComplete(List<PlanItem> list) {
        showData(list);
    }

    public void showData(List<PlanItem> list){
//        ArrayList<String> count = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++){
//            int num = DataSupport.where( "planId = ?", list.get(i).getPlanId() + "").count(PlanItem.class);
//            count.add( num + "");
//        }
        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager);
        PlanItemAdapter adapter = new PlanItemAdapter(this, list);
        recyclerView.setAdapter( adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return true;
    }
}
