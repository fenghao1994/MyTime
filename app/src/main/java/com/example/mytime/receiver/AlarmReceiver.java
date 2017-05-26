package com.example.mytime.receiver;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.ui.activity.TransActivity;
import com.example.mytime.util.Extra;

import cn.smssdk.SMSSDK;

public class AlarmReceiver extends BroadcastReceiver {
    private SMSSDK.VerifyCodeReadListener listener;
    private PlanItem planItem;
    private Context mContext;
    private boolean flag; //false 表示闹钟， true表示地点

    public AlarmReceiver(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.listener = verifyCodeReadListener;
    }

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("xxxxxxxxxxxxxxxxx", intent.getAction());
        this.mContext = context;
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Extra.ALARM_CLOCK)) {
                planItem = (PlanItem) intent.getSerializableExtra("PLAN_ITEM");
                flag = false;
                gotoAlarm();
                Log.i("AlarmReceiver", "闹钟响了" + flag);
            } else if (intent.getAction().equals(Extra.ALARM_LOCATION)) {
                planItem = (PlanItem) intent.getSerializableExtra("PLAN_ITEM");
                planItem.setDescribe("LOCATED");
                planItem.update(planItem.getId());
                flag = true;
                gotoAlarm();
                Log.i("AlarmReceiver", "到达目的地附近" + flag);
            } else if (intent.getAction().equals(Extra.SMS_RECEIVER)) {
                getSMS(context, intent);
            }
        }
    }

    public void gotoAlarm(){
        Intent intent = new Intent(mContext, TransActivity.class);
        intent.putExtra("FLAG", flag);
        intent.putExtra("PLAN_ITEM", planItem);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity( intent);
    }

    public void getSMS(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] smsArr = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                smsArr[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            for (SmsMessage sms : smsArr) {
                if (sms != null) {
                    SMSSDK.readVerificationCode(sms, listener);
                }
            }
        }// END if(bundle != null)
    }


}
