package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.presenter.ICreatePlanPresenter;
import com.example.mytime.mvp.presenter.impl.CreatePlanPresenterImpl;
import com.example.mytime.mvp.ui.adapter.PlanItemAdapter;
import com.example.mytime.mvp.ui.custom.TitleDialog;
import com.example.mytime.mvp.ui.view.ICreatePlanView;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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
    private SharedPreferences sp;

    private ICreatePlanPresenter createPlanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        createPlanPresenter = new CreatePlanPresenterImpl(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        plan = (Plan) getIntent().getSerializableExtra("PLAN");
        isFromFab = getIntent().getBooleanExtra("IS_FROM_FAB", false);

        if (plan != null) {
            createPlanPresenter.showData(plan);
        } else {
            createPlanInData();
        }
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
    }

    @Override
    public void showData(List<PlanItem> planItems) {
        toolbarTitle.setText("编辑");

        if (planItems != null && planItems.size() > 0) {
            adapter = new PlanItemAdapter(this, planItems);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (plan != null) {
            createPlanPresenter.showData(plan);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST) {
                isCompletePlanItem = true;
            }
        }
    }

    public void clickOk() {

        final TitleDialog titleDialog = new TitleDialog(this, plan.getTitle());
        titleDialog.show();

        Window window = titleDialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.height = (int)(display.getHeight() * 0.2);
        layoutParams.width = (int) (display.getWidth() * 0.9);
        window.setAttributes(layoutParams);

        titleDialog.setResultListener(new TitleDialog.ResultListener() {
            @Override
            public void onResultListener(String str) {
                if (!TextUtils.isEmpty(str)) {
                    if (isCompletePlanItem && plan != null) {
                        if (isFromFab) {
                            plan.setTitle(str);
                            createPlanPresenter.savePlan(plan);
                            //等于2 上传到服务器
                            if (Extra.NET_WORK == 2){
                                saveObjectWithNetWork();
                            }

                        } else {
                            plan.setTitle(str);
                            editPlanData();
                            createPlanPresenter.updatePlan(plan);
                            //等于2 上传到服务器
                            if (Extra.NET_WORK == 2){
                                updateObjectWithNetWork();
                            }
                        }
                        titleDialog.dismiss();
                        Toast.makeText(CreatePlanActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (plan != null && plan.getTitle() != null) {
                            if (!plan.getTitle().equals(str)) {
                                plan.setTitle(str);
                                editPlanData();
                                if (isFromFab){
                                    createPlanPresenter.savePlan(plan);
                                }else {
                                    createPlanPresenter.updatePlan(plan);
                                }


                                //等于2 上传到服务器
                                if (Extra.NET_WORK == 2){
                                    saveObjectWithNetWork();
                                }
                            }
                            Toast.makeText(CreatePlanActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                            titleDialog.dismiss();
                            return;
                        }
                        Toast.makeText(CreatePlanActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                        titleDialog.dismiss();
                        return;
                    }
                } else {
                    Toast.makeText(CreatePlanActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void complete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 创建一个新的plan对象
     */
    public void createPlanInData() {
        planCreateTime = System.currentTimeMillis();
        planEditTime = planCreateTime;
        planPlanId = planCreateTime;
        planTitle = "";
        planIsExpired = false;
        planIsComplete = false;
        planIsEdit = false;

        plan = new Plan();

        plan.setPlanId(planPlanId);
        plan.setCreateTime(planCreateTime);
        plan.setEdit(planIsEdit);
        plan.setExpired(planIsExpired);
        plan.setEditTime(planEditTime);
        plan.setComplete(planIsComplete);
        plan.setTitle(planTitle);
    }

    /**
     * 编辑plan对象
     *
     * @return
     */
    public void editPlanData() {
        planEditTime = System.currentTimeMillis();
//        planTitle = "";
        planIsEdit = true;

        plan.setEdit(planIsEdit);
        plan.setEditTime(planEditTime);
//        plan.setTitle(planTitle);
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
        if (isCompletePlanItem && plan != null && isFromFab) {
            plan.setTitle("");
            createPlanPresenter.savePlan(plan);
            //等于2 上传到服务器
            if (Extra.NET_WORK == 2){
                saveObjectWithNetWork();
            }
        }
        this.finish();
    }

    public void saveObjectWithNetWork(){
        postUrl(HttpUrl.POST_SAVE_PLAN);
    }

    public void updateObjectWithNetWork(){
        postUrl(HttpUrl.POST_UPDATA_PLAN);
    }

    public void postUrl(String url){
        OkHttpUtils
                .post()
                .url(url)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", plan.getId() + "")
                .addParams("planId", plan.getPlanId() + "")
                .addParams("title", plan.getTitle())
                .addParams("isExpired", plan.isExpired() + "")
                .addParams("isComplete", plan.isComplete() + "")
                .addParams("createTime", plan.getCreateTime() + "")
                .addParams("editTime", plan.getEditTime() + "")
                .addParams("isEdit", plan.isEdit() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
}
