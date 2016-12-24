package com.example.mytime.mvp.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.view.ILogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILogin {


    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.input_verification_code)
    EditText inputVerificationCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;

    //多一秒 因为是先减去一秒后才发送的message
    private static final int TIME_OUT = 6;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.text_login)
    TextView textLogin;

    //是否可以点击获取验证码
    private boolean isClickVerificationCode;
    private int second = TIME_OUT;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getVerificationCode.setText("再次发送(" + second + ")");
            if (second <= 0) {
                second = TIME_OUT;
                isClickVerificationCode = true;
                getVerificationCode.setEnabled(isClickVerificationCode);
                getVerificationCode.setText("获取验证码");
                handler.removeCallbacks(runnable);
            }
            super.handleMessage(msg);
        }
    };


    //获取验证码时间的runnable
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Message message = new Message();
                    message.what = 1;
                    Thread.sleep(1000);
                    second -= 1;
                    handler.sendMessage(message);
                    if (second <= 0) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isClickVerificationCode = true;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.logo);
        }

    }

    @Override
    public boolean checkPhoneNumberValidity(String str) {
        return false;
    }

    @Override
    public boolean checkVerificationCodeValidity(String str) {
        return false;
    }

    @OnClick({R.id.phone_number, R.id.input_verification_code, R.id.get_verification_code, R.id.text_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_number:
                break;
            case R.id.input_verification_code:
                break;
            case R.id.get_verification_code:
                clickVerificationCode();
                break;
            case R.id.text_login:
                clickLogin();
                break;
        }
    }

    //点击获取验证码按钮
    private void clickVerificationCode() {
        if (isClickVerificationCode) {
            isClickVerificationCode = false;
            getVerificationCode.setEnabled(isClickVerificationCode);
            getVerificationCode.setText("获取验证码");
            new Thread(runnable).start();
        }
    }

    //验证成功跳转
    private void clickLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

