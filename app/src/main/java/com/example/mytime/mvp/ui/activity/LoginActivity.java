package com.example.mytime.mvp.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.view.ILoginView;
import com.example.mytime.receiver.AlarmReceiver;
import com.example.mytime.util.Extra;
import com.example.mytime.util.MyUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends AppCompatActivity implements ILoginView {


    private static final String DEFAULT_COUNTRY_ID = "42";
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.input_verification_code)
    EditText inputVerificationCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;

    //多一秒 因为是先减去一秒后才发送的message
    private static final int TIME_OUT = 60;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.text_login)
    TextView textLogin;

    private BroadcastReceiver smsReceiver;
    private EventHandler eventHandler;

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

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSMSSDK();
        initSMSReceiver();
    }

    public void initView() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.logo);
        }
        textLogin.setEnabled(false);
        textLogin.setBackgroundColor(Color.GREEN);
    }

    public void initSMSSDK() {
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        Log.i("LoginActivity-complete", data.toString());
                    } else {
                        Log.i("LoginActivity-error", data.toString());
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(LoginActivity.this, new String(data.toString()), Toast.LENGTH_SHORT).show();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void initSMSReceiver() {
        smsReceiver = new AlarmReceiver(new SMSSDK.VerifyCodeReadListener() {
            public void onReadVerifyCode(final String verifyCode) {
                showVerifyCode(verifyCode);
            }
        });
        this.registerReceiver(smsReceiver, new IntentFilter(Extra.SMS_RECEIVER));
    }

    @UiThread
    public void showVerifyCode(String verifyCode) {
        inputVerificationCode.setText(verifyCode);
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

    @OnTextChanged(value = R.id.input_verification_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void verificationTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            textLogin.setEnabled(true);
            textLogin.setBackgroundColor(Color.RED);
        } else {
            textLogin.setEnabled(false);
            textLogin.setBackgroundColor(Color.GREEN);
        }
    }

    //点击获取验证码按钮
    private void clickVerificationCode() {
        checkMobileNumber();
        if (isClickVerificationCode) {
            isClickVerificationCode = false;
            getVerificationCode.setEnabled(isClickVerificationCode);
            getVerificationCode.setText("获取验证码");
            new Thread(runnable).start();
        }
        SMSSDK.getVerificationCode("+86", phoneNumber.getText().toString().trim(), new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });
    }

    //验证成功跳转
    private void clickLogin() {
        checkMobileNumber();
        if (!TextUtils.isEmpty(phoneNumber.getText().toString().trim()) && !TextUtils.isEmpty(inputVerificationCode.getText().toString().trim())) {
            SMSSDK.submitVerificationCode("+86", phoneNumber.getText().toString().trim(), inputVerificationCode.getText().toString().trim());
        }
    }

    /**
     * 检查手机号是否正确 不正确直接返回
     */
    public void checkMobileNumber() {
        if (!MyUtil.isMobileNumber(phoneNumber.getText().toString().trim())) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(smsReceiver);
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

