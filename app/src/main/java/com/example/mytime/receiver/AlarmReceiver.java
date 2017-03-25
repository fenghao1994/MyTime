package com.example.mytime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mytime.util.Extra;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null){
            if (intent.getAction().equals(Extra.ALARM_CLOCK)){
                Toast.makeText( context, "闹钟响了", Toast.LENGTH_SHORT).show();
                Log.e("ALARM", "xxxxxxxxxxxxxxx");
            }else if (intent.getAction().equals(Extra.ALARM_LOCATION)){
                Toast.makeText(context, "到达目的地附近", Toast.LENGTH_SHORT).show();
                Log.e("ALARM_LOCATION", "xxxxxxxxxxxxxxx");
            }
        }
    }
}
