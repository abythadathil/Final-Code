package com.app.noisetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class StopRecorder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
   //     Toast.makeText(context,"Stopping..",Toast.LENGTH_LONG).show();

        Intent alarmIntent = new Intent(context, EmailService.class);

        alarmIntent.putExtra("start","no");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(alarmIntent);
        }else{
            context.startService(alarmIntent);
        }
    }
}
