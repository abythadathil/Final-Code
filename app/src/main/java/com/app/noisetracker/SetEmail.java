package com.app.noisetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appyplus.soundmeter.R;

public class SetEmail extends AppCompatActivity {

    EditText email;

    Button submit;
    Button back;
    Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_email);

        submit = findViewById(R.id.submitemail);
        back = findViewById(R.id.back2);
        main = findViewById(R.id.Main);
        email = findViewById(R.id.emailentry);

        SharedPreferences sh = getSharedPreferences("EmailPref", MODE_PRIVATE);



        String temail = sh.getString("email","");

        email.setText(temail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getSharedPreferences("EmailPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                Toast.makeText(SetEmail.this,"Email Set",Toast.LENGTH_LONG).show();

                myEdit.putString("email",email.getText().toString());

                myEdit.apply();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetEmail.super.onBackPressed();
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetEmail.this,MainActivity.class));
            }
        });


    }
}