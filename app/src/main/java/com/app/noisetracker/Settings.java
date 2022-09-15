package com.app.noisetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appyplus.soundmeter.R;

public class Settings extends AppCompatActivity {

    Button EmailFreq;

    Button Recordsch;

    Button back;

    Button exit;

    Button configemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);


        EmailFreq = findViewById(R.id.email2);

        back = findViewById(R.id.back);

        Recordsch = findViewById(R.id.recordfreq);

        exit = findViewById(R.id.exit2);

        configemail = findViewById(R.id.email);

        configemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,SetEmail.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Settings.this)
                        .setTitle("Do you want to Exit")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                finishAffinity();


                            }
                        }).create().show();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.super.onBackPressed();
            }
        });

        EmailFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,EmailFrequency.class));
            }
        });

        Recordsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,RecordingSchedule.class));
            }
        });
    }
}