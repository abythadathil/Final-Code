package com.app.noisetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.noisetracker.R;

public class RecordingSchedule extends AppCompatActivity {

    CardView d1,d2,d3,d4,d5,d6,d7;

    TextView tSunday,tMonday,tTuesday,tWednesday,tThursday,tFriday,tSaturday;

    Button main10, config10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_schedule);

        d1 = findViewById(R.id.day1);
        d2 = findViewById(R.id.day2);
        d3 = findViewById(R.id.day3);
        d4 = findViewById(R.id.day4);
        d5 = findViewById(R.id.day5);
        d6 = findViewById(R.id.day6);
        d7 = findViewById(R.id.day7);

        main10 = findViewById(R.id.main10);
        config10 = findViewById(R.id.config10);

        main10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,MainActivity.class));
            }
        });

        config10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Settings.class));
            }
        });

        tSunday = findViewById(R.id.tSunday);
        tMonday = findViewById(R.id.tMonday);
        tTuesday = findViewById(R.id.tTuesday);
        tWednesday = findViewById(R.id.tWednesday);
        tThursday = findViewById(R.id.tThursday);
        tFriday = findViewById(R.id.tFriday);
        tSaturday = findViewById(R.id.tSaturday);

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Sunday.class));
            }
        });

        d2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Monday.class));
            }
        });

        d3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Tuesday.class));
            }
        });

        d4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Wednesday.class));
            }
        });

        d5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Thursday.class));
            }
        });

        d6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Friday.class));
            }
        });

        d7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordingSchedule.this,Saturday.class));
            }
        });


        SharedPreferences sh = getSharedPreferences("SUNDAY", MODE_PRIVATE);


        String stat1 = sh.getString("radiovalued","Always Record");

        if(stat1.contentEquals("Schedule Time")){

            tSunday.setText("Timer Scheduled");
        }else if(stat1.contentEquals("Always")){
            tSunday.setText("Always Recording on Sunday");
        }else if(stat1.contentEquals("No Recording")){
            tSunday.setText("No Recording on Sunday");
        }

        SharedPreferences sh2 = getSharedPreferences("MONDAY", MODE_PRIVATE);


        String stat2 = sh2.getString("radiovalued","Always Record");

        if(stat2.contentEquals("Schedule Time")){

            tMonday.setText("Timer Scheduled");
        }else if(stat2.contentEquals("Always")){
            tMonday.setText("Always Recording on Monday");
        }else if(stat2.contentEquals("No Recording")){
            tMonday.setText("No Recording on Monday");
        }


        SharedPreferences sh3 = getSharedPreferences("TUESDAY", MODE_PRIVATE);


        String stat3 = sh3.getString("radiovalued","Always Record");

        if(stat3.contentEquals("Schedule Time")){

            tTuesday.setText("Timer Scheduled");
        }else if(stat3.contentEquals("Always")){
            tTuesday.setText("Always Recording on Tuesday");
        }else if(stat3.contentEquals("No Recording")){
            tTuesday.setText("No Recording on Tuesday");
        }

        SharedPreferences sh4 = getSharedPreferences("WEDNESDAY", MODE_PRIVATE);


        String stat4 = sh4.getString("radiovalued","Always Record");

        if(stat4.contentEquals("Schedule Time")){

            tWednesday.setText("Timer Scheduled");
        }else if(stat4.contentEquals("Always")){
            tWednesday.setText("Always Recording on Wednesday");
        }else if(stat4.contentEquals("No Recording")){
            tWednesday.setText("No Recording on Wednesday");
        }

        SharedPreferences sh5 = getSharedPreferences("THURSDAY", MODE_PRIVATE);



        String stat5 = sh5.getString("radiovalued","Always Record");

        if(stat5.contentEquals("Schedule Time")){

            tThursday.setText("Timer Scheduled");
        }else if(stat5.contentEquals("Always")){
            tThursday.setText("Always Recording on Thursday");
        }else if(stat5.contentEquals("No Recording")){
            tThursday.setText("No Recording on Thursday");
        }

        SharedPreferences sh6 = getSharedPreferences("FRIDAY", MODE_PRIVATE);



        String stat6 = sh6.getString("radiovalued","Always Record");

        if(stat6.contentEquals("Schedule Time")){

            tFriday.setText("Timer Scheduled");
        }else if(stat6.contentEquals("Always")){
            tFriday.setText("Always Recording on Friday");
        }else if(stat6.contentEquals("No Recording")){
            tFriday.setText("No Recording on Friday");
        }


        SharedPreferences sh7 = getSharedPreferences("SATURDAY", MODE_PRIVATE);


        String stat7 = sh7.getString("radiovalued","Always Record");

        if(stat7.contentEquals("Schedule Time")){

            tSaturday.setText("Timer Scheduled");
        }else if(stat7.contentEquals("Always")){
            tSaturday.setText("Always Recording on Saturday");
        }else if(stat7.contentEquals("No Recording")){
            tSaturday.setText("No Recording on Saturday");
        }





    }



}