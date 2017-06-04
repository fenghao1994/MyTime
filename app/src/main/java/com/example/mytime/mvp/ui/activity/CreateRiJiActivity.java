package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.RiJi;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class CreateRiJiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.riji_content)
    AutoCompleteTextView rijiContent;
    @BindView(R.id.activity_create_ri_ji)
    LinearLayout activityCreateRiJi;
    @BindView(R.id.weather)
    TextView weather;
    @BindView(R.id.calendar)
    TextView calendar;

    private RiJi riJi;
    private String flag;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ri_ji);
        ButterKnife.bind(this);
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        calendar.setText(MyUtil.dateYMD(System.currentTimeMillis()));
        flag = getIntent().getStringExtra("FLAG");
        if (!TextUtils.isEmpty(flag) && flag.equals("EDIT")) {
            ok.setVisibility(View.GONE);
            rijiContent.setEnabled(false);
            riJi = (RiJi) getIntent().getSerializableExtra("DATE");
            weather.setText("天气：" + riJi.getWeather());
            calendar.setText(MyUtil.dateYMD(riJi.getCreateTime()));
            rijiContent.setText(riJi.getContent());
            toolbarTitle.setText("我的日记");
        }
        if (Extra.getEntity() != null){
            weather.setText("天气: " + Extra.getEntity().getResult().get(0).getWeather());
        }

    }
    @OnClick(R.id.ok)
    public void saveRiJi(){
        if (!TextUtils.isEmpty(rijiContent.getText().toString().trim())){
            RiJi riJi = new RiJi();
            riJi.setContent(rijiContent.getText().toString());
            riJi.setCreateTime(System.currentTimeMillis());
            if(Extra.getEntity() != null){
                riJi.setWeather(Extra.getEntity().getResult().get(0).getWeather());
            }else {
                riJi.setWeather("晴");
            }
            riJi.save();
            if (Extra.NET_WORK == 2){
                riJi = DataSupport.where("createTime = ?", riJi.getCreateTime() + "").findFirst(RiJi.class);
                sendMessage(riJi);
            }
            finish();
        }else {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(RiJi riJi){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_RIJI)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", riJi.getId() + "")
                .addParams("content", riJi.getContent())
                .addParams("createTime", riJi.getCreateTime() + "")
                .addParams("weather", riJi.getWeather())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(CreateRiJiActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(CreateRiJiActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
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
