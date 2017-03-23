package com.example.mytime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText( context, "闹钟响了", Toast.LENGTH_SHORT).show();
        Log.e("ALARM", "xxxxxxxxxxxxxxx");
    }
}
