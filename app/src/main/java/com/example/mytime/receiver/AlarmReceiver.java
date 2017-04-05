package com.example.mytime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.mytime.util.Extra;

import cn.smssdk.SMSSDK;

public class AlarmReceiver extends BroadcastReceiver {
    private SMSSDK.VerifyCodeReadListener listener;

    public AlarmReceiver(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.listener = verifyCodeReadListener;
    }
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null){
            if (intent.getAction().equals(Extra.ALARM_CLOCK)){
                Toast.makeText( context, "闹钟响了", Toast.LENGTH_SHORT).show();
                Log.e("ALARM", "xxxxxxxxxxxxxxx");
            }else if (intent.getAction().equals(Extra.ALARM_LOCATION)){
                Toast.makeText(context, "到达目的地附近", Toast.LENGTH_SHORT).show();
                Log.e("ALARM_LOCATION", "xxxxxxxxxxxxxxx");
            }else if (intent.getAction().equals(Extra.SMS_RECEIVER)){
                getSMS(context, intent);
            }
        }
    }

    public void getSMS(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] smsArr = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                smsArr[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            for (SmsMessage sms: smsArr) {
                if(sms != null) {
                    SMSSDK.readVerificationCode(sms, listener);
                }
            }
        }// END if(bundle != null)
    }
}
