package com.app.noisetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import static android.content.Context.MODE_PRIVATE;

public class EmailAlarm extends BroadcastReceiver {

    String path;

    private static final String FILE_NAME = "data.csv";

    public static String sqlSelect =
            "SELECT decibels, time, type, location " +
                    "FROM noise";

    public final String host = "34.123.81.5";

    public final String database = "postgres";
    public final int port = 5432;
    public final String user = "postgres";
    public final String pass = "Noise";
    public String url = "jdbc:postgresql://%s:%d/%s";

    String savepath;

    public String email;

    SharedPreferences sh;

    private static final String CSV_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;

    /*
        檔案副檔名
     */
    private static final String CSV_FILE__NAME_EXTENSION = ".csv";

    String pathname;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Sending Email..",Toast.LENGTH_LONG).show();



        this.url = String.format(this.url, this.host, this.port, this.database);

        connect2(context);

        pathname = CSV_FILE_PATH + "Noise" + CSV_FILE__NAME_EXTENSION;

        sh = context.getSharedPreferences("EmailPref", MODE_PRIVATE);

        email = sh.getString("email","");


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over


                try {
                    sendEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 1000);


    }

    public void connect2(Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //          FileOutputStream fileWriter = null;
                String col = "ID,NAME,EMAIL,COUNTRY,PASSWORD";

                String Path = Environment.getExternalStorageDirectory().toString() + "/Pictures/SMA/test.csv";

                File f = new File("Noise.csv");




                List<List<String>> contents = new ArrayList<>();



                try (

                        Connection c = DriverManager.getConnection(url, user, pass);

                        Statement statement = c.createStatement();




                        // write header line containing column names

                        ResultSet resultSet = statement.executeQuery(sqlSelect)){

        //            FileOutputStream fos = context.openFileOutput("data.txt",MODE_PRIVATE);

      //              CSVWriter writer = new CSVWriter(new FileWriter(Path));






                    while (resultSet.next()) {

                        String decibels = resultSet.getString("decibels");
                        String time = resultSet.getString("time");
                        String type = resultSet.getString("type");
                        String location = resultSet.getString("location");


                        String line = String.format(" %s |, %s |, %s |, %s |",
                                decibels,time,type,location) + "\n";

                        String line2[] = {decibels,time,type,location};
                        List<String> oneLineColumns = new ArrayList<>();

                        oneLineColumns.add(decibels);
                        oneLineColumns.add(time);
                        oneLineColumns.add(type);
                        oneLineColumns.add(location);

                        contents.add(oneLineColumns);



                        //                 Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();



         //               fos.write(line.getBytes());
        //                writer.writeNext(line2);



                        //                 fileWriter.newLine();
                        //                 fileWriter.write(line);
                    }

                    CsvWriter.toCsvFile(contents);

                    //                      statement.close();
                    //                      fos.close();



        //            savepath = context.getFilesDir() + "/" + "data.txt";

                    savepath = pathname;


                }catch (SQLException e){

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

    public void sendEmail() {

        String subject = "Noise Tracker Update";
   //     String message = value + " Type: " + MainActivity.mainActivityRunningInstance.noisetype;

        //Creating SendMail object
        SendMail sm = new SendMail(email, "Noise Tracker Update","Please find attached Noise details",pathname);


        //Executing sendmail to send email
        sm.execute();
    }



}

