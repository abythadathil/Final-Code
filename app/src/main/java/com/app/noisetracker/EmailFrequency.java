package com.app.noisetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appyplus.soundmeter.R;

import java.time.LocalDate;
import java.util.Calendar;

public class EmailFrequency extends AppCompatActivity {

    private RadioGroup radioGroup;

    RadioButton rd1, rd2, rd3;

    TextView tim;

    int selecthour1 = 0;
    int selectmin1 = 0;

    int selecthour2 = 0;
    int selectmin2 = 0;

    String[] title;
    String spinner_item;

    Button back,main,exit;

    TimePicker timePicker;

    SpinnerAdapter adapter;

    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_frequency);

        title = getResources().getStringArray(R.array.titles);
        adapter=new SpinnerAdapter(getApplicationContext());

        rd2 = findViewById(R.id.radia_id2);

        rd3 = findViewById(R.id.radia_id3);

        rd1 = findViewById(R.id.radia_id1);

        tim = findViewById(R.id.textView7);

        back = findViewById(R.id.back5);
        main = findViewById(R.id.main5);
        exit = findViewById(R.id.exit5);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailFrequency.this,Settings.class));
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailFrequency.this, MainActivity.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EmailFrequency.this)
                        .setTitle("Do you want to Exit")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                finishAffinity();


                            }
                        }).create().show();
            }
        });

        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tim.setText("Email will be sent Every Hour");

                long currenttime = Calendar.getInstance().getTimeInMillis();

                setAlarm();

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();


                myEdit.putInt("mychoice", 0);

                myEdit.putString("radiovalue", "Every 1 hour");

                myEdit.apply();


            }
        });

        rd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                cancelAllPreviousAlarm();

                final Dialog dialog = new Dialog(EmailFrequency.this);
                dialog.setContentView(R.layout.layout_weekly);
                dialog.setCancelable(true);

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // write all the data entered by the user in SharedPreference and apply


                myEdit.putInt("mychoice", 2);

                myEdit.putString("radiovalue", "Weekly");




                final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
                spinner.setAdapter(adapter);

                final Button button = dialog.findViewById(R.id.submit);

                timePicker = dialog.findViewById(R.id.timePicker);





                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        spinner_item = title[position];

                        myEdit.putString("day", spinner_item );

                        day = position + 1;




     //                   Toast.makeText(EmailFrequency.this,spinner_item,Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });




                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tim.setText( "Email will be sent every " + spinner_item +" at " + timePicker.getHour() + ":" + timePicker.getMinute());
                        selecthour2 = timePicker.getHour();
                        selectmin2 = timePicker.getMinute();



                        myEdit.putInt("hour2",selecthour2);
                        myEdit.putInt("min2",selectmin2);
                        myEdit.putString("day", spinner_item );
                        myEdit.apply();




                 //       setAlarmweekly(c.getTimeInMillis());

                        dialog.cancel();

                        Toast.makeText(EmailFrequency.this, "Email configured..", Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(EmailFrequency.this,MainActivity.class));
                    }
                });



                dialog.show();

            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EmailFrequency.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tim.setText( "Email will be sent everyday at " + selectedHour + ":" + selectedMinute);
                        selecthour1 = selectedHour;
                        selectmin1 = selectedMinute;

                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putInt("mychoice", 1);

                        myEdit.putString("radiovalue", "Everyday");

                        myEdit.putInt("hour1",selecthour1);
                        myEdit.putInt("min1",selectmin1);

                        myEdit.apply();

                        Calendar c = Calendar.getInstance();

                        c.setTimeInMillis(System.currentTimeMillis());

                        c.set(Calendar.HOUR_OF_DAY,selecthour1);
                        c.set(Calendar.MINUTE,selectmin1);
                        c.set(Calendar.SECOND,1);


                        setAlarmeveryday(c);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        radioGroup = (RadioGroup)findViewById(R.id.groupradio11);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);


        int i = sh.getInt("mychoice", -1);

        String x = sh.getString("radiovalue","");
        String day = sh.getString("day", "");

        if(i>-1){
            RadioButton saved = (RadioButton)radioGroup.getChildAt(i);
            saved.setChecked(true);
        }

        int hour1 = sh.getInt("hour1",0);
        int min1 = sh.getInt("min1", 0);

        int hour2 = sh.getInt("hour2",0);
        int min2 = sh.getInt("min2",0);


        if(x.contentEquals("Every 1 hour")){
            tim.setText("Email will be sent every hour");


        }else if(x.contentEquals("Everyday")){
            tim.setText("Email will be sent every day at " + hour1 + ":" + min1);


        }else if(x.contentEquals("Weekly")){
            tim.setText("Email will be sent on " + day + " at " + hour2 + ":" + min2);



            //        Toast.makeText(MainActivity.this,today,Toast.LENGTH_LONG).show();






        }


        //     radioGroup.clearCheck();

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

                        if(val.contentEquals("Everyday")){

                        }else if(val.contentEquals("Weekly")){


                        }



                    }
                });
    }



    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called


    private void setAlarm() {

   //     cancelAllPreviousAlarm();
        //getting the alarm manager



        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1500,AlarmManager.INTERVAL_HOUR,pi);

        Toast.makeText(this, "Email configured..", Toast.LENGTH_SHORT).show();
    }

    private void setAlarmeveryday(Calendar c) {

   //     cancelAllPreviousAlarm();
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

        Toast.makeText(this, "Email configured " + c.get(Calendar.HOUR_OF_DAY)+ ":" + c.get(Calendar.MINUTE) , Toast.LENGTH_SHORT).show();
    }

    private void setAlarmweekly(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, EmailAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE);

        //setting the repeating alarm that will be fired every day
        am.set(AlarmManager.RTC, time, pi);

        Toast.makeText(this, "Email configured..", Toast.LENGTH_SHORT).show();
    }

    void cancelAllPreviousAlarm() {
        //acquire the dedicated PendingIntent
        PendingIntent pendingIntentAllPrevious = PendingIntent.getBroadcast(this, 0, new Intent(this, EmailAlarm.class), PendingIntent.FLAG_NO_CREATE);
        if (pendingIntentAllPrevious != null) {
            try {
                pendingIntentAllPrevious.send(this, 0, null, new PendingIntent.OnFinished() {
                    @Override
                    public void onSendFinished(PendingIntent pendingIntent, Intent intent, int resultCode, String resultData, Bundle resultExtras) {
                        for (Parcelable parcelable : intent.getParcelableArrayExtra("previous")) {
                            //alarm will cancel when the corresponding PendingIntent cancel
                            ((PendingIntent) parcelable).cancel();
                        }
                    }
                }, null);
                pendingIntentAllPrevious.cancel();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }


    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview, null);
                holder = new ListContent();
                holder.text = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {
                holder = (ListContent) v.getTag();
            }

            holder.text.setText(title[position]);

            return v;
        }
    }

    static class ListContent {
        TextView text;
    }
}
