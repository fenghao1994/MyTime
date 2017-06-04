package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PingLun;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.HttpUrl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PingLunActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_ping_lun)
    LinearLayout activityPingLun;
    @BindView(R.id.pinglun_context)
    AutoCompleteTextView pinglunContext;

    private User user;
    private PlanItem planItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_lun);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }
        user = DataSupport.findFirst(User.class);
        planItem = (PlanItem) getIntent().getSerializableExtra("PLANITEM");
    }

    @OnClick(R.id.ok)
    public void okClick() {
        String str = pinglunContext.getText().toString().trim();
        if (TextUtils.isEmpty(str)){
            Toast.makeText(this, "请先填入评论", Toast.LENGTH_SHORT).show();
            return ;
        }
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_SEND_PINGLUN)
                .addParams("content", pinglunContext.getText().toString())
                .addParams("phoneNumber", user.getPhoneNumber())
                .addParams("createTime", planItem.getCreateTime() + "")
                .addParams("editTime", System.currentTimeMillis() + "")
                .addParams("deletePinglun",  "false")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PingLunActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(PingLunActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        finish();
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
