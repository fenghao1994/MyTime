package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MyFeedBackActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.feed_back)
    AutoCompleteTextView feedBack;
    @BindView(R.id.send_message)
    TextView sendMessage;
    @BindView(R.id.activity_my_feed_back)
    LinearLayout activityMyFeedBack;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed_back);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        if (Extra.NET_WORK == 1){
            activityMyFeedBack.setVisibility(View.GONE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(feedBack.getText().toString())){
                    Toast.makeText(MyFeedBackActivity.this, "请输入反馈意见", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendFeedBack();
            }
        });
    }

    public void sendFeedBack(){
        OkHttpUtils.post()
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("createTime", System.currentTimeMillis() + "")
                .addParams("feedback", feedBack.getText().toString())
                .url(HttpUrl.POST_FEED_BACK)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyFeedBackActivity.this, "提交失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MyFeedBackActivity.this, "提交成功,感谢您的反馈", Toast.LENGTH_SHORT).show();
                        MyFeedBackActivity.this.finish();
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
