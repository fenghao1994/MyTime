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
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.HttpUrl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ChangeMessageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_text)
    AutoCompleteTextView editText;
    @BindView(R.id.activity_change_message)
    LinearLayout activityChangeMessage;
    private String flag;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }

        user = DataSupport.findFirst(User.class);
        flag = getIntent().getStringExtra("FLAG");
        if (flag.equals("NAME")){
            if (TextUtils.isEmpty(user.getUserName())){
                editText.setHint("请输入名字");
            }else {
                editText.setText(user.getUserName());
                editText.setSelection(user.getUserName().length());
            }
        }else if (flag.equals("SIGNATURE")){
            if (TextUtils.isEmpty(user.getPersonalizedSignature())){
                editText.setHint("请输入个性签名");
            }else {
                editText.setText(user.getPersonalizedSignature());
                editText.setSelection(user.getPersonalizedSignature().length());
            }
        }else if (flag.equals("INTRODUCTION")){
            if (TextUtils.isEmpty(user.getSelfIntroduction())){
                editText.setHint("请输入自我介绍");
            }else {
                editText.setText(user.getSelfIntroduction());
                editText.setSelection(user.getSelfIntroduction().length());
            }
        }
    }

    @OnClick(R.id.ok)
    public void okClick(){
        if (!TextUtils.isEmpty(editText.getText().toString().trim())){
            String str = editText.getText().toString().trim();
            if (flag.equals("NAME")){
                user.setUserName(str);
            }else if (flag.equals("SIGNATURE")){
                user.setPersonalizedSignature(str);
            }else if (flag.equals("INTRODUCTION")){
                user.setSelfIntroduction(str);
            }
            user.saveOrUpdate();
            updateUserInfo();
        }else {
            Toast.makeText(this, "请填入信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserInfo(){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_USER_MESSAGE)
                .addParams("phoneNumber", user.getPhoneNumber())
                .addParams("personalizedSignature", user.getPersonalizedSignature() == null ? "":  user.getPersonalizedSignature())
                .addParams("selfIntroduction", user.getSelfIntroduction() == null ? "" : user.getSelfIntroduction())
                .addParams("userName", user.getUserName() == null ? "" : user.getUserName())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ChangeMessageActivity.this, "修改失败,请稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(ChangeMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
