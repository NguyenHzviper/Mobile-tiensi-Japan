package com.nvn.mobilent.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nvn.mobilent.R;
import com.nvn.mobilent.screens.login.LoginActivity;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Intro.this, LoginActivity.class);
                startActivity(i);
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 1800);
    }
}