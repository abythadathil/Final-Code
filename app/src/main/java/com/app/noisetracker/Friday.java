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

public class Friday extends AppCompatActivity {

    public RadioGroup radioGroup;

    RadioButton rd1, rd2, rd3;

    TextView status;

    Button start, stop;

    int Fridayh1=0,Fridaym1=0,Fridayh2=0,Fridaym2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friday);

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

                status.setText("No Recording for Friday ");
            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                status.setText("Always Recording for Friday");
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
                mTimePicker = new TimePickerDialog(Friday.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //                status.setText( "Recording will be done on Sunday between " + selectedHour + ":" + selectedMinute);


                        Fridayh1 = selectedHour;
                        Fridaym1 = selectedMinute;

                        SharedPreferences sharedPreferences = getSharedPreferences("FRIDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putInt("hour1",Fridayh1);
                        myEdit.putInt("min1",Fridaym1);

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
                mTimePicker = new TimePickerDialog(Friday.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //                status.setText( "Recording will be done on Sunday between " + selectedHour + ":" + selectedMinute);
                        Fridayh2 = selectedHour;
                        Fridaym2 = selectedMinute;

                        SharedPreferences sharedPreferences = getSharedPreferences("FRIDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putInt("hour2",Fridayh2);
                        myEdit.putInt("min2",Fridaym2);

                        myEdit.apply();

                        status.setText("Recording set between " + Fridayh1 + ":" + Fridaym1 +" to " + Fridayh2 + ":" + Fridaym2 );

                        Toast.makeText(Friday.this, "Recorder set..", Toast.LENGTH_SHORT).show();


                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Stop Time");
                mTimePicker.show();
            }
        });




        SharedPreferences sh = getSharedPreferences("FRIDAY", MODE_PRIVATE);


        int i = sh.getInt("mychoiced6", -1);

        String stat = sh.getString("radiovalued","");

        int h1 = sh.getInt("hour1",0);
        int h2 = sh.getInt("hour2",0);
        int m1 = sh.getInt("min1",0);
        int m2 = sh.getInt("min2",0);

        status.setText(stat);

        if(stat.contentEquals("Schedule Time")){

            status.setText("Recording set between " + h1 + ":" + m1 +" to " + h2 + ":" + m2 );
        }else if(stat.contentEquals("Always")){
            status.setText("Always Record on Friday");
        }else if(stat.contentEquals("No Recording")){
            status.setText("No Recording on Friday");
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

                        SharedPreferences sharedPreferences = getSharedPreferences("FRIDAY", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        // write all the data entered by the user in SharedPreference and apply

                        int i = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

                        myEdit.putInt("mychoiced6", radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId())));





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

        startActivity(new Intent(Friday.this,RecordingSchedule.class));

    }
}