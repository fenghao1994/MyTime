package com.example.mytime.mvp.ui.activity;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;

/**
 * 透明activity
 */
public class TransActivity extends AppCompatActivity {

    private PlanItem planItem;
    private Ringtone ringtone;
    private Vibrator vib;
    private AlertDialog alertDialog;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        planItem = (PlanItem) getIntent().getSerializableExtra("PLAN_ITEM");
        flag = getIntent().getBooleanExtra("FLAG", false);

        if (planItem != null){
            if ( !flag){
                alarmIng();
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle(planItem.getTitle())
                        .setMessage(planItem.getContent())
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                if (vib != null){
                                    vib.cancel();
                                }
                                if (ringtone != null && ringtone.isPlaying()){
                                    ringtone.stop();
                                }
                                finish();
                            }
                        })
                        .show();
            }else {
                arrvide();
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle(planItem.getTitle())
                        .setMessage(planItem.getContent())
                        .setPositiveButton("目的地在你附近,我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                if (vib != null){
                                    vib.cancel();
                                }
                                if (ringtone != null && ringtone.isPlaying()){
                                    ringtone.stop();
                                }
                                finish();
                            }
                        })
                        .show();
            }

        }
    }

    //闹钟想起来
    public void alarmIng() {
        vibrate();
        ringAlarm();
        if (!TextUtils.isEmpty(planItem.getPhoneNumber())) {
            callPhone();
        }
        if (!TextUtils.isEmpty(planItem.getMessagePhoneNumber())){
            sendSMS();
        }
    }

    //地点到了
    public void arrvide(){
        vibrate();
        ringAlarm();
    }

    //打电话
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + planItem.getPhoneNumber()));
        this.startActivity(intent);
    }



    //闹钟响了 有要发短信的要求
    public void sendSMS(){

        if(PhoneNumberUtils.isGlobalPhoneNumber(planItem.getMessagePhoneNumber())){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+planItem.getMessagePhoneNumber()));
            intent.putExtra("sms_body", planItem.getMessageContent());
            this.startActivity(intent);
        }
    }

    //震动
    public void vibrate(){
        vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        long[] patter = new long[]{1000, 2000, 1000, 2000, 1000, 2000};
        vib.vibrate(patter, -1);
    }

    //响闹钟铃声
    public void ringAlarm(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this,notification);
        ringtone.play();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (vib != null){
            vib.cancel();
        }
        if (ringtone != null && ringtone.isPlaying()){
            ringtone.stop();
        }
        this.finish();
    }
}
