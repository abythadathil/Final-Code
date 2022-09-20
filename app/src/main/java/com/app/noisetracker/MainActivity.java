package com.app.noisetracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noisetracker.R;
import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.tensorflow.lite.support.audio.TensorAudio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivityRunningInstance;
    public static MainActivity  getInstace(){
        return mainActivityRunningInstance;
    }

    TextView mStatusView;
    MediaRecorder mRecorder = null;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    public static final String MODEL_FILE = "lite-model_yamnet_classification_tflite_1.tflite";
    public static final float MINIMUM_DISPLAY_THRESHOLD = 0.3f;


    TextView result;

    TensorAudio audioTensor;

    Button exit;


    Button settings;

    Button Audioclass;


    String modelPath = "lite-model_yamnet_classification_tflite_1.tflite";

    Float probabilityThreshold = 0.3f;

    TextView NoiseType;

    AudioRecord record;

    String today;




    public static String fileName = null;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };
    final Handler mHandler = new Handler();

    SharedPreferences sh;

    public static String emailshared;

    public static String noisevalue,noisetype;

    public static int val;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    public static String latitude="",longitude="";

    DigitSpeedView digitSpeedView;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

try {
    File root = Environment.getExternalStorageDirectory();

    if (root.canWrite()) {

        File fileDir = new File(root.getAbsolutePath());
        fileDir.mkdir();

        File file = new File(fileDir, "samplefile.txt");

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fileWriter);
        out.write("Hello");
        out.close();
    }

}catch (IOException e){

}

        mainActivityRunningInstance = this;

        if(EmailService.mpRecorder != null){
            EmailService.stopRecorder();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();




        sh = getSharedPreferences("EmailPref", MODE_PRIVATE);

        emailshared = sh.getString("email","");


        mStatusView = (TextView) findViewById(R.id.value);

        digitSpeedView = (DigitSpeedView)findViewById(R.id.digitSpeedView);

        digitSpeedView.updateSpeed(0);


        result = findViewById(R.id.result);

        exit = findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Do you want to Exit")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                finishAffinity();


                            }
                        }).create().show();
            }
        });


        settings = findViewById(R.id.Settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Settings.class));
            }
        });



        //     gauge1 = findViewById(R.id.gauge1);






        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(500);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String x = sh.getString("radiovalue","");

        int hour1 = sh.getInt("hour1",0);
        int min1 = sh.getInt("min1", 0);

        int hour2 = sh.getInt("hour2",0);
        int min2 = sh.getInt("min2",0);

        String day = sh.getString("day", "");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now().getDayOfWeek().name();
        }


        SharedPreferences sh3 = getSharedPreferences(today, MODE_PRIVATE);


        String stat2 = sh3.getString("radiovalued","nothing");

  //      Toast.makeText(MainActivity.this,stat2,Toast.LENGTH_LONG).show();




        if(stat2.contains("Always") || stat2.contains("nothing")){

            Intent alarmIntent = new Intent(MainActivity.this, EmailService.class);

            alarmIntent.putExtra("start","yes");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(alarmIntent);
            }else{
                startService(alarmIntent);
            }


        }else if(stat2.contains("No Recording")){
            Intent alarmIntent = new Intent(MainActivity.this, EmailService.class);

            alarmIntent.putExtra("start","no");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(alarmIntent);
            }else{
                startService(alarmIntent);
            }
        }else if(stat2.contains("Schedule Time")){

            SharedPreferences sh1 = getSharedPreferences(today, MODE_PRIVATE);



            int h1 = sh1.getInt("hour1",0);
            int h2 = sh1.getInt("hour2",0);
            int m1 = sh1.getInt("min1",0);
            int m2 = sh1.getInt("min2",0);

            Calendar c1 = Calendar.getInstance();

            c1.setTimeInMillis(System.currentTimeMillis());

            c1.set(Calendar.HOUR_OF_DAY,h1);
            c1.set(Calendar.MINUTE,m1);

            setStartAlarm(c1);

            Calendar c2 = Calendar.getInstance();

            c2.setTimeInMillis(System.currentTimeMillis());

            c2.set(Calendar.HOUR_OF_DAY,h2);
            c2.set(Calendar.MINUTE,m2);

            setStopAlarm(c2);


        }




        if(x.contentEquals("Every 1 hour")){

            long currenttime = Calendar.getInstance().getTimeInMillis();

  //          setAlarm(currenttime + 2000);
            

        }else if(x.contentEquals("Everyday")){

            Calendar c = Calendar.getInstance();

            c.set(Calendar.HOUR_OF_DAY,hour1);
            c.set(Calendar.MINUTE,min1);


   //         setAlarmeveryday(c.getTimeInMillis());


        }else if(x.contentEquals("Weekly")){

            Calendar calendar = Calendar.getInstance();
            int day2 = calendar.get(Calendar.DAY_OF_WEEK);
            String today= "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                today = LocalDate.now().getDayOfWeek().name();
            }

    //        Toast.makeText(MainActivity.this,today,Toast.LENGTH_LONG).show();


            if(today.contains(day)){

                Calendar c = Calendar.getInstance();

                c.set(Calendar.HOUR_OF_DAY,hour2);
                c.set(Calendar.MINUTE, min2);

                Date cu = Calendar.getInstance().getTime();

                if(cu.before(c.getTime())) {
                    setAlarmweekly(c);
                }

            }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_HOUR, pi);

        Toast.makeText(this, "Email configured..", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarmeveryday(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);

        Toast.makeText(this, "Email configured..", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarmweekly(Calendar c) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

//        Toast.makeText(this, "Email configured..", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStartAlarm(Calendar c) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, StartRecorder.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setStopAlarm(Calendar c) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, StopRecorder.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);


    }

    public void onResume()
    {
        super.onResume();

 //       startRecorder();

    }





    public void onPause()
    {
        super.onPause();
 //       stopRecorder();

  //      stopRecorder();


    }

    public void startRecorder(){


        if (mRecorder == null)
        {
            try {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(fileName);

                mRecorder.prepare();
                //           Thread.sleep(1000);


                //          mRecorder.start();
            }catch (IOException e) {
                e.printStackTrace();
            }
            //	   Thread.sleep(1000);
            mRecorder.start();






            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){

        int val = (int) EmailService.soundDb();

        if(val < 0){
            val = 0;
        }
        //      mStatusView.setText(Double.toString((soundDb())) + " dB");

        mStatusView.setText(Integer.toString(val) + " dB");

   //     Toast.makeText(MainActivity.this,Integer.toString(val) + " dB",Toast.LENGTH_LONG).show();

        noisevalue = Integer.toString(val) + " dB";



        digitSpeedView.updateSpeed(val);



        result.setText(EmailService.noisetype);


    }
    public double soundDb(){
        return  20 * Math.log10(getAmplitude());
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        // check if permissions are given


        // check if location is enabled
        if (isLocationEnabled()) {

            // getting last
            // location from
            // FusedLocationClient
            // object
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude = String.valueOf(location.getLatitude());
                        longitude= String.valueOf(location.getLongitude());

  //                      Toast.makeText(MainActivity.this,latitude+":"+longitude,Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.LOCATION_SERVICE);
            startActivity(intent);
        }
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude= String.valueOf(mLastLocation.getLatitude());
            longitude= String.valueOf(mLastLocation.getLongitude());

    //        Toast.makeText(MainActivity.this,latitude+":"+longitude,Toast.LENGTH_LONG).show();
        }
    };

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}

