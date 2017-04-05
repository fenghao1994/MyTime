package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.presenter.ICreatePlanPresenter;
import com.example.mytime.mvp.presenter.impl.CreatePlanPresenterImpl;
import com.example.mytime.mvp.ui.adapter.PlanItemAdapter;
import com.example.mytime.mvp.ui.view.ICreatePlanView;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePlanActivity extends AppCompatActivity implements ICreatePlanView {

    public static final int REQUEST = 1;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_create_plan)
    CoordinatorLayout activityCreatePlan;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Plan plan;
    long planCreateTime;
    long planEditTime;
    long planPlanId;
    String planTitle;
    boolean planIsExpired;
    boolean planIsComplete;
    boolean planIsEdit;

    boolean isCompletePlanItem;
    PlanItemAdapter adapter;

    private boolean isFromFab;


    private ICreatePlanPresenter createPlanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        ButterKnife.bind(this);
        setSupportActionBar( toolbar);
        createPlanPresenter = new CreatePlanPresenterImpl( this);

        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager);

        plan = (Plan) getIntent().getSerializableExtra("PLAN");
        isFromFab = getIntent().getBooleanExtra("IS_FROM_FAB", false);

        if ( plan != null){
            createPlanPresenter.showData( plan);
        }else {
            createPlanInData();
        }
    }

    @Override
    public void showData(List<PlanItem> planItems) {
        toolbarTitle.setText("编辑");

        if ( planItems != null && planItems.size() > 0){
            adapter = new PlanItemAdapter(this, planItems);
            recyclerView.setAdapter( adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( plan != null){
            createPlanPresenter.showData( plan);
        }
    }

    @OnClick({R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.recycler_view, R.id.activity_create_plan, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.ok:
                clickOk();
                break;
            case R.id.toolbar:
                break;
            case R.id.recycler_view:
                break;
            case R.id.activity_create_plan:
                break;
            case R.id.fab:
                createPlanItem();
                break;
        }
    }



    private void createPlanItem() {
        Intent intent = new Intent(this, CreatePlanItemActivity.class);
        intent.putExtra("PLAN", plan);
        isCompletePlanItem = false;
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if ( requestCode == REQUEST){
                isCompletePlanItem = true;
            }
        }
    }

    public void clickOk(){
        if ( isCompletePlanItem && plan != null){
            if ( isFromFab){
                createPlanPresenter.savePlan( plan);
            }else {
                editPlanData();
                createPlanPresenter.updatePlan( plan);
            }
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void complete(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 创建一个新的plan对象
     */
    public void createPlanInData(){
        planCreateTime = System.currentTimeMillis();
        planEditTime = planCreateTime;
        planPlanId = planCreateTime;
        planTitle = "";
        planIsExpired = false;
        planIsComplete = false;
        planIsEdit = false;

        plan = new Plan();

        plan.setPlanId( planPlanId);
        plan.setCreateTime( planCreateTime);
        plan.setEdit( planIsEdit);
        plan.setExpired( planIsExpired);
        plan.setEditTime( planEditTime);
        plan.setComplete( planIsComplete);
        plan.setTitle( planTitle);
    }

    /**
     * 编辑plan对象
     * @return
     */
    public void editPlanData(){
        planEditTime = System.currentTimeMillis();
        planTitle = "";
        planIsEdit = true;

        plan.setEdit( planIsEdit);
        plan.setEditTime( planEditTime);
        plan.setTitle( planTitle);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
