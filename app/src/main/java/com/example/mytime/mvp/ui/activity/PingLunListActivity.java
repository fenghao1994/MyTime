package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PingLun;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.adapter.PingLunListAdapter;
import com.example.mytime.util.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PingLunListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.pinglun)
    ImageView pinglun;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.activity_ping_lun_list)
    LinearLayout activityPingLunList;

    private PlanItem planItem;
    private User user;

    private PingLunListAdapter pingLunListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_lun_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        planItem = (PlanItem) getIntent().getSerializableExtra("PLANITEM");
        user = DataSupport.findFirst(User.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    @OnClick(R.id.pinglun)
    public void goPingLun(){
        Intent intent = new Intent(this, PingLunActivity.class);
        intent.putExtra("PLANITEM", planItem);
        startActivity(intent);
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


    public void getMessage(){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_ALL_PING_LUN)
                .addParams("createTime", planItem.getCreateTime() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PingLunListActivity.this, "获取评论失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<PingLun>>() {
                        }.getType();
                        ArrayList<PingLun> list = gson.fromJson(response, type);

                        if (list != null && list.size() > 0){
                            pingLunListAdapter = new PingLunListAdapter(PingLunListActivity.this, list);
                            listview.setAdapter(pingLunListAdapter);
                        }
                    }
                });
    }
}
