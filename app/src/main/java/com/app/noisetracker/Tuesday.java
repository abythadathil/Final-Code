package com.app.noisetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.noisetracker.R;

import java.util.Calendar;

public class Tuesday extends AppCompatActivity {

    public RadioGroup radioGroup;

    RadioButton rd1, rd2, rd3;

    TextView status;

    Button start, stop;

    int Tuesdayh1=0,Tuesdaym1=0,Tuesdayh2=0,Tuesdaym2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuesday);

        rd2 = findViewById(R.id.radia_id2);

        rd3 = findViewById(R.id.radia_id3);

        rd1 = findViewById(R.id.radia_id1);

        status = findViewById(R.id.textView11);

        start = findViewById(R.id.starts);

        stop = findViewById(R.id.stops);

        radioGroup = (RadioGroup)findViewById(R.id.groupradio1);

        radioGroup.clearCheck();


        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);

                status.setText("No Recording for Tuesday ");
            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);

                status.setText("Always Recording for Tuesday");
            }
        });

        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);

        rd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Tuesday.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //                status.setText( "Recording will be done on Sunday between " + selectedHour + ":" + selectedMinute);


                        Tuesdayh1 = selectedHour;
                        Tuesdaym1 = selectedMinute;

                        SharedPreferences sharedPreferences = getSharedPreferences("TUESDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putInt("hour1",Tuesdayh1);
                        myEdit.putInt("min1",Tuesdaym1);

                        myEdit.apply();

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Tuesday.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //                status.setText( "Recording will be done on Sunday between " + selectedHour + ":" + selectedMinute);
                        Tuesdayh2 = selectedHour;
                        Tuesdaym2 = selectedMinute;

                        SharedPreferences sharedPreferences = getSharedPreferences("TUESDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putInt("hour2",Tuesdayh2);
                        myEdit.putInt("min2",Tuesdaym2);

                        myEdit.apply();

                        status.setText("Recording set between " + Tuesdayh1 + ":" + Tuesdaym1 +" to " + Tuesdayh2 + ":" + Tuesdaym2 );

                        Toast.makeText(Tuesday.this, "Recorder set..", Toast.LENGTH_SHORT).show();


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Stop Time");
                mTimePicker.show();
            }
        });



        SharedPreferences sh = getSharedPreferences("TUESDAY", MODE_PRIVATE);


        int i = sh.getInt("mychoiced3", -1);

        String stat = sh.getString("radiovalued","");

        int h1 = sh.getInt("hour1",0);
        int h2 = sh.getInt("hour2",0);
        int m1 = sh.getInt("min1",0);
        int m2 = sh.getInt("min2",0);

        status.setText(stat);

        if(stat.contentEquals("Schedule Time")){

            status.setText("Recording set between " + h1 + ":" + m1 +" to " + h2 + ":" + m2 );
        }else if(stat.contentEquals("Always")){
            status.setText("Always Recording on Tuesday");
        }else if(stat.contentEquals("No Recording")){
            status.setText("No Recording on Tuesday");
        }




        if (i > -1) {
            RadioButton saved = (RadioButton) radioGroup.getChildAt(i);
            saved.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);

                        String val = (String) radioButton.getText();

                        SharedPreferences sharedPreferences = getSharedPreferences("TUESDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        // write all the data entered by the user in SharedPreference and apply

                        int i = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

                        myEdit.putInt("mychoiced3", radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId())));





                        int x = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

                        RadioButton z = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                        String radiovalue = (String) z.getText();
                        myEdit.putString("radiovalued", radiovalue);

                        myEdit.apply();



                    }
                });



    }
    @Override
    public void onBackPressed(){

        startActivity(new Intent(Tuesday.this,RecordingSchedule.class));

    }
}