package com.app.noisetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;

public class StartRecorder extends BroadcastReceiver {

    String today;
    @Override
    public void onReceive(Context context, Intent intent) {


  //      Toast.makeText(context,"Starting..",Toast.LENGTH_LONG).show();

        Intent alarmIntent = new Intent(context, EmailService.class);

        alarmIntent.putExtra("start","yes");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(alarmIntent);
        }else{
            context.startService(alarmIntent);
        }
    }
}
