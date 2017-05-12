package com.example.mytime.mvp.ui.activity;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
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
import com.example.mytime.mvp.ui.view.ILoginView;
import com.example.mytime.receiver.AlarmReceiver;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements ILoginView {


    private static final String DEFAULT_COUNTRY_ID = "42";
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.input_verification_code)
    EditText inputVerificationCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.layout_voice)
    RelativeLayout layoutVoice;
    @BindView(R.id.voice_sms)
    TextView voiceSMS;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    //多一秒 因为是先减去一秒后才发送的message
    private static final int TIME_OUT = 60;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.text_login)
    TextView textLogin;
    @BindView(R.id.layout_login)
    RelativeLayout mLayoutLogin;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private BroadcastReceiver smsReceiver;
    private EventHandler eventHandler;

    //是否可以点击获取验证码
    private boolean isClickVerificationCode;

    //是否点击了获取语音验证码
    private boolean isClickVoiceSMS;
    private int second = TIME_OUT;
    //控制语音发送短信的layout是否可见，当一次60s完了后，可见
    private int clickSendSMSCount = 0;

    private int voiceSecond = TIME_OUT;

    private SharedPreferences sp;

    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getVerificationCode.setText("再次发送(" + second + ")");
                if (second <= 0) {
                    second = TIME_OUT;
                    isClickVerificationCode = true;
                    getVerificationCode.setEnabled(isClickVerificationCode);
                    getVerificationCode.setText("获取验证码");
                    handler.removeCallbacks(runnable);
                    clickSendSMSCount = 1;
                }
                if (clickSendSMSCount == 1) {
                    layoutVoice.setVisibility(View.VISIBLE);
                }
            } else if (msg.what == 2) {
                voiceSMS.setText("再次发送（" + voiceSecond + ")");
                if (voiceSecond <= 0) {
                    voiceSecond = TIME_OUT;
                    isClickVoiceSMS = true;
                    voiceSMS.setEnabled(isClickVoiceSMS);
                    voiceSMS.setText("短信没收到？用电话接收吧!");
                    handler.removeCallbacks(runnable);
                }
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
    //获取语音验证码runnable
    private Runnable voiceRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    Message message = new Message();
                    message.what = 2;
                    Thread.sleep(1000);
                    voiceSecond -= 1;
                    handler.sendMessage(message);
                    if (voiceSecond <= 0) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }
        isClickVerificationCode = true;
        isClickVoiceSMS = true;
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        initView();
        progressDialog = new ProgressDialog(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        initSMSSDK();
        initSMSReceiver();
    }

    public void initView() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }
        mLayoutLogin.setEnabled(false);
        mLayoutLogin.setBackgroundColor(getResources().getColor(R.color.darkGray));
    }

    public void initSMSSDK() {
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        Log.i("Register-complete", data.toString());
                        detileMessage("验证码发送成功");
                    } else {
                        detileMessage(data);
                        Log.i("RegisterActivity-error", data.toString());
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        detileMessage("验证成功");
                        sumbitInfo();
                        finish();
                        Log.i("RegisterActivity-verifi", data.toString());
                    } else {
                        detileMessage(data);
                        Log.i("RegisterActivity-error", data.toString());
                    }
                    progressBarHide();

                } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        detileMessage("请等待，电话短信拨号中");
                    } else {
                        detileMessage(data);
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sumbitInfo() {
        progressDialog.setMessage("正在注册");
        progressDialog.show();;
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_REGISTER)
                .addParams("phoneNumber", phoneNumber.getText().toString().trim())
                .addParams("password", password.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "服务器异常,请稍后在试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        sp.edit().putString("phoneNumber", phoneNumber.getText().toString().trim())
                                .putString("password", password.getText().toString())
                                .commit();
                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(response, Map.class);
                        Toast.makeText(RegisterActivity.this, map.get("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    public void detileMessage(Object data) {
        Looper.prepare();
        Gson gson = new Gson();
        String message = "";
        Map<String, String> map = new HashMap<>();
        if (data != null) {
            if (data instanceof Throwable) {
                message = ((Throwable) data).getMessage();
                map = gson.fromJson(message, Map.class);
                showToast(map.get("detail"));
            } else {
                message = data.toString();
                showToast(message);
            }
        }
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

    @OnClick({R.id.phone_number, R.id.input_verification_code, R.id.get_verification_code, R.id.text_login, R.id.voice_sms, R.id.layout_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_number:
                break;
            case R.id.input_verification_code:
                break;
            case R.id.get_verification_code:
                clickVerificationCode();
                break;
            case R.id.layout_login:
                clickLogin();
                break;
            case R.id.voice_sms:
                clickVoiceSMS();
                break;
        }
    }

    private void clickVoiceSMS() {
        checkMobileNumber();
        if (isClickVoiceSMS) {
            isClickVoiceSMS = false;
            voiceSMS.setEnabled(isClickVoiceSMS);
            voiceSMS.setText("短信没收到？用电话接收吧!");
            new Thread(voiceRunnable).start();
        }
        SMSSDK.getVoiceVerifyCode("+86", phoneNumber.getText().toString().trim());
    }

    @OnTextChanged(value = R.id.input_verification_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void verificationTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            mLayoutLogin.setEnabled(true);
            mLayoutLogin.setBackgroundColor(getResources().getColor(R.color.logoGreen));
        } else {
            mLayoutLogin.setEnabled(false);
            mLayoutLogin.setBackgroundColor(getResources().getColor(R.color.darkGray));
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
        if (TextUtils.isEmpty(password.getText().toString().trim())) {
            Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(inputVerificationCode.getText().toString().trim())) {
            SMSSDK.submitVerificationCode("+86", phoneNumber.getText().toString().trim(), inputVerificationCode.getText().toString().trim());
            progressBarShow();
        } else {
            Toast.makeText(this, "请先输入验证码", Toast.LENGTH_SHORT).show();
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

   /* private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }*/

    /**
     * 隐藏progressbar
     */
    public void progressBarHide() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 显示progressbar
     */
    public void progressBarShow() {
        progressBar.setVisibility(View.VISIBLE);
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

