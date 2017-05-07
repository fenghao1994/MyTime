package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.http_callback.UserCallBack;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.text_login)
    TextView textLogin;
    @BindView(R.id.layout_login)
    RelativeLayout layoutLogin;
    @BindView(R.id.goRegister)
    TextView goRegister;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = checkInfo();
                if (flag){
                    postLoginInfo();
                }
            }
        });

        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        if ( !"".equals(sp.getString("phoneNumber", ""))){
            goMainActivity();
        }
    }

    public boolean checkInfo(){
        if ( !TextUtils.isEmpty(phoneNumber.getText().toString().trim())  && MyUtil.isMobileNumber(phoneNumber.getText().toString().trim())
                && !TextUtils.isEmpty(password.getText().toString().trim())){
            return true;
        }else {
            Toast.makeText(this, "请确认手机号和密码", Toast.LENGTH_SHORT).show();
            return false;
        }
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


    public void postLoginInfo(){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_LOGIN)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .addParams("password", password.getText().toString().trim())
                .build()
                .execute(new UserCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        sp.edit().putString("phoneNumber", response.getPhoneNumber())
                                .putString("password", response.getPassword())
                                .commit();
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
