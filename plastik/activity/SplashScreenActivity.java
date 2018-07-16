package com.teamproject.plastikproject.plastik.activity;

import android.os.Bundle;
import android.os.Handler;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.helper.SessionManager;


public class SplashScreenActivity extends SessionManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager.checkLogin();
                finish();
            }
        },4000);

    }
}
