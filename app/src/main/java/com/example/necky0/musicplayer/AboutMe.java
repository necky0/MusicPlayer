package com.example.necky0.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutMe.class);
        context.startActivity(starter);
    }
}
