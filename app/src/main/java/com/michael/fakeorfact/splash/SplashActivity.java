package com.michael.fakeorfact.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.michael.fakeorfact.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start Main activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        // close splash activity
        finish();
    }
}
