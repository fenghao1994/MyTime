package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.adapter.FriendOpenPlanItemAdapter;
import com.example.mytime.util.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 从好友页面进入朋友的公开的计划
 */
public class FriendOpenPlanItemActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private User user;
    private FriendOpenPlanItemAdapter planItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_open_plan_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        user = (User) getIntent().getSerializableExtra("USER");
        getMessage();
    }

    public void getMessage() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_SOMEONE_OPEN_PLANITEM)
                .addParams("phoneNumber", user.getPhoneNumber())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String a = "";
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<PlanItem>>() {
                        }.getType();
                        ArrayList<PlanItem> list = gson.fromJson(response, type);

                        planItemAdapter = new FriendOpenPlanItemAdapter(FriendOpenPlanItemActivity.this, list);
                        recyclerView.setAdapter(planItemAdapter);
                    }
                });
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
