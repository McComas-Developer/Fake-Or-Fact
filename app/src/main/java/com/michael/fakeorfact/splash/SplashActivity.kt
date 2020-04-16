package com.michael.fakeorfact.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.fakeorfact.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start Main activity
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))

        // close splash activity
        finish()
    }
}