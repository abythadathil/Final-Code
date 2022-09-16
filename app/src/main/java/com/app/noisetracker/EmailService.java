package com.app.noisetracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.app.noisetracker.R;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmailService extends Service {

    public static MediaRecorder mpRecorder = null;



    public static String fName = null;

    Handler handler = new Handler();

    public static int val;

    public AudioClassifier classifier;
    public AudioRecord mAudioRecord;

    String modelPath = "lite-model_yamnet_classification_tflite_1.tflite";
    public static final float MINIMUM_DISPLAY_THRESHOLD = 0.3f;
    AudioRecord record;

    public static String noisetype;

    public Connection connection;

    public static final String createTableSQL = "CREATE TABLE IF NOT EXISTS noise " +
            "(DECIBELS TEXT, " +
            " TIME TEXT, " +
            " TYPE TEXT, " +
            " LOCATION TEXT)";

    public static final String INSERT_USERS_SQL = "INSERT INTO noise" +
            " (decibels, time, type, location) VALUES " +
            "(?, ?, ?, ?)";

    TensorAudio audioTensor;

    public final String host = "34.123.81.5";

    public final String database = "postgres";
    public final int port = 5432;
    public final String user = "postgres";
    public final String pass = "Noise";
    public String url = "jdbc:postgresql://%s:%d/%s";;
    public boolean status;

    public Context context;

    String today;

    String audiostat = "Recording..";

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    String latitude="",longitude="";

    public static Boolean runningstat = true;







    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        fName = getExternalCacheDir().getAbsolutePath();
        fName += "/audiorecordtest.3gp";

        Bundle b = intent.getExtras();

        if(b!=null){
            String x = (String) b.get("start");

            if(x.contains("yes")){
                runningstat = true;
      //          Toast.makeText(EmailService.this,"Starting Recorder..",Toast.LENGTH_LONG).show();

            }else if(x.contains("no")){
                runningstat = false;

      //          Toast.makeText(EmailService.this,"Stopping Recorder..",Toast.LENGTH_LONG).show();

            }
        }



        this.url = String.format(this.url, this.host, this.port, this.database);

        connect();


        try {
            classifier = AudioClassifier.createFromFile(this,modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioTensor = classifier.createInputTensorAudio();

        record = classifier.createAudioRecord();
        record.startRecording();





        createNotificationChannel();

        Intent intent1 = new Intent(this,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,PendingIntent.FLAG_IMMUTABLE);





        if(runningstat == true) {

            startAudio();

            Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                    .setContentTitle("Noise Tracker")
                    .setContentText("Recording..")
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent).build();

            startForeground(1, notification);
        }else if(runningstat == false){

            stopRecorder();

            record.stop();

            Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                    .setContentTitle("Noise Tracker")
                    .setContentText("Not Recording..")
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent).build();

            startForeground(1, notification);


        }







        return START_STICKY;

    }





    public void startAudio(){





        Runnable code = new Runnable() {
            @Override
            public void run() {

                audioTensor.load(record);
                List<Classifications> output = classifier.classify(audioTensor);
                List<Category> filterModelOutput = new ArrayList<>();
                for(Classifications classifications : output) {
                    for(Category category: classifications.getCategories()){
                        if(category.getScore() > MINIMUM_DISPLAY_THRESHOLD){
                            filterModelOutput.add(category);
                        }
                    }

                }
                for(Category category: filterModelOutput){

                    noisetype = category.getLabel();
                }

                if(mpRecorder != null) {
                    val = (int) soundDb();
                    String db = String.valueOf(val) + " DB";

                    SimpleDateFormat sdf = new SimpleDateFormat("dd|MM|yyyy HH:mm:ss", Locale.getDefault());
                    String currentDnT = sdf.format(new Date());

                    if (val > 75) {

                        if(status == true) {

                            post(db, currentDnT, noisetype);
                        }
                    }


   //                 Toast.makeText(EmailService.this, db + noisetype, Toast.LENGTH_LONG).show();
                }


                handler.postDelayed(this,500);

            }
        };



        handler.post(code);

        startRecorder();


    }

    public void connect()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    //          connection = DriverManager.getConnection(url, user, pass);

                    connection = DriverManager.getConnection(url,user,pass);
                    status = true;
                    System.out.println("connected:" + status);

                    Statement statement = connection.createStatement();

                    // Step 3: Execute the query or update query
                    statement.execute(createTableSQL);


                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }

    public void post(String db, String date, String type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (

                        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){


                    preparedStatement.setString(1, db);
                    preparedStatement.setString(2, date);
                    preparedStatement.setString(3, type);
                    preparedStatement.setString(4, "Lat-"+MainActivity.latitude+":Long-"+MainActivity.longitude);

                    preparedStatement.executeUpdate();



                } catch (SQLException e){



                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1","Foreground notifiction", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }


    }

   

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    public void startRecorder(){


        if (mpRecorder == null)
        {
            try {
                mpRecorder = new MediaRecorder();
                mpRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mpRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mpRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mpRecorder.setOutputFile(fName);

                mpRecorder.prepare();
                //           Thread.sleep(1000);


                //          mRecorder.start();
            }catch (IOException e) {
                e.printStackTrace();
            }
            //	   Thread.sleep(1000);
            mpRecorder.start();






            //mEMA = 0.0;
        }

    }

    public static void stopRecorder() {
        if (mpRecorder != null) {
            mpRecorder.stop();
            mpRecorder.release();
            mpRecorder = null;
        }
    }

    public static double soundDb(){
        return  20 * Math.log10(getAmplitude()/0.6325);
    }
    public static double getAmplitude() {
        if (mpRecorder != null)
            return  (mpRecorder.getMaxAmplitude());
        else
            return 0;

    }


}