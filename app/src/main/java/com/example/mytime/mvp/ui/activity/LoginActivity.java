package com.example.mytime.mvp.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.RiJi;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

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
    @BindView(R.id.goForgetPassword)
    TextView goForgetPassword;
    private SharedPreferences sp;

    private ProgressDialog progressDialog;

    boolean b1, b2, b3, b4;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }

        layoutLogin.setEnabled(false);
        layoutLogin.setBackgroundColor(getResources().getColor(R.color.darkGray));

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = checkInfo();
                if (flag) {
                    postLoginInfo();
                }
            }
        });

        if (Extra.NET_WORK == 1) {
            goMainActivity();
        }

        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        if (!"".equals(sp.getString("phoneNumber", ""))) {
            goMainActivity();
        }
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    layoutLogin.setEnabled(true);
                    layoutLogin.setBackgroundColor(getResources().getColor(R.color.logoGreen));
                } else {
                    layoutLogin.setEnabled(false);
                    layoutLogin.setBackgroundColor(getResources().getColor(R.color.darkGray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        progressDialog = new ProgressDialog(this);
    }

    public boolean checkInfo() {
        if (!TextUtils.isEmpty(phoneNumber.getText().toString().trim()) && MyUtil.isMobileNumber(phoneNumber.getText().toString().trim())
                && !TextUtils.isEmpty(password.getText().toString().trim())) {
            return true;
        } else {
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


    public void postLoginInfo() {
        progressDialog.setMessage("正在登陆");
        progressDialog.show();
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_LOGIN)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .addParams("password", password.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        sp.edit().putString("phoneNumber", phoneNumber.getText().toString().trim())
                                .putString("password", password.getText().toString())
                                .commit();

                        /*user = new User();
                        user.setPhoneNumber(phoneNumber.getText().toString().trim());
                        user.setPassword(password.getText().toString());
                        user.save();*/
                        Gson gson = new Gson();
                        user = gson.fromJson(response, User.class);
                        user.save();
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        getNotesFromNet();
                        getPlanItemFromNet();
                        getPlanFromNet();
                        getRijiFromNet();
                    }
                });
    }

    public void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.goForgetPassword)
    public void setGoForgetPassword() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("FLAG", true);
        startActivity(intent);
    }

    public void getPlanItemFromNet() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_PLAN_ITEM)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<PlanItem>>() {
                        }.getType();
                        ArrayList<PlanItem> list = gson.fromJson(response, type);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).save();
                                for (int j = 0; j < list.get(i).getAddress().size(); j++) {
                                    list.get(i).getAddress().get(j).save();
                                    downLoadImg(list.get(i).getAddress().get(j).getAddress(), list.get(i).getAddress().get(j).getCreateTime() + "");
                                }
                            }
                        }
                        b1 = true;
                        goMainFromNet();
                    }
                });

    }

    public void downLoadImg(String url, String name) {
        url = url.substring(3, url.length());
        url = url.replace("\\", "/");
        OkHttpUtils//
                .get()//
                .url(HttpUrl.ROOT + "/" + url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), name + ".png")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String a = "";
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        List<Photo> list = DataSupport.findAll(Photo.class);
                        for (int i = 0; i < list.size(); i++){
                            if (response.getAbsolutePath().contains(list.get(i).getCreateTime() + "")){
                                list.get(i).setAddress(response.getAbsolutePath());
                                list.get(i).save();
                            }
                        }
                    }
                });
    }

    public void getNotesFromNet() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_NOTE)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Note>>() {
                        }.getType();
                        ArrayList<Note> list = gson.fromJson(response, type);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).save();
                                for (int j = 0; j < list.get(i).getAddress().size(); j++) {
                                    list.get(i).getAddress().get(j).save();
                                    downLoadImg(list.get(i).getAddress().get(j).getAddress(), list.get(i).getAddress().get(j).getCreateTime() + "");
                                }
                            }
                        }
                        b2 = true;
                        goMainFromNet();
                    }
                });
    }

    public void getPlanFromNet() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_PLAN)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Plan>>() {
                        }.getType();
                        ArrayList<Plan> list = gson.fromJson(response, type);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).save();
                            }
                        }
                        b3 = true;
                        goMainFromNet();
                    }
                });
    }

    public void getRijiFromNet() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_ALL_RIJI)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<RiJi>>() {
                        }.getType();
                        ArrayList<RiJi> list = gson.fromJson(response, type);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).save();
                            }
                        }
                        b4 = true;
                        goMainFromNet();
                    }
                });
    }

    public void goMainFromNet() {
        if (b1 && b2 && b3 && b4) {
            progressDialog.dismiss();
            goMainActivity();
        }
    }
}
