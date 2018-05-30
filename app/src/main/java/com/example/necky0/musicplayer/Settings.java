package com.example.necky0.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity {

    RadioButton noRepeat;
    RadioButton repeatOne;
    RadioButton repeatAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initListeners();
        setCheckedStatus();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, Settings.class);
        context.startActivity(starter);
    }

    private void setCheckedStatus() {
        String option = MainActivity.getRadioStatus();

        if(option.equals(MainActivity.RADIO_NONE)){
            noRepeat.setChecked(true);
        }
        else if(option.equals(MainActivity.RADIO_ONE)){
            repeatOne.setChecked(true);
        }
        else if(option.equals(MainActivity.RADIO_ALL)){
            repeatAll.setChecked(true);
        }
    }

    private void initViews() {
        noRepeat = findViewById(R.id.noRepeat);
        repeatOne = findViewById(R.id.repeatOne);
        repeatAll = findViewById(R.id.repeatAll);
    }

    private void initListeners() {
        noRepeat.setOnClickListener(noRepeatListener);
        repeatOne.setOnClickListener(repeatOneListener);
        repeatAll.setOnClickListener(repeatAllListener);
    }

    View.OnClickListener noRepeatListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.setRadioStatus(MainActivity.RADIO_NONE);
        }
    };


    View.OnClickListener repeatOneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.setRadioStatus(MainActivity.RADIO_ONE);
        }
    };


    View.OnClickListener repeatAllListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.setRadioStatus(MainActivity.RADIO_ALL);
        }
    };

}
