package com.michael.fakeorfact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView ic_settings;      // Setting Icon
        Button quiz;                // Quiz Button
        Button start;               // Start Game Button
        Button join;                // Join Game Button

        setContentView(R.layout.activity_main);
        // Set Settings OnClick
        ic_settings = findViewById(R.id.img_icon);
        ic_settings.setOnClickListener(this);

        // Set Quiz OnClick
        quiz = findViewById(R.id.btn_quiz);
        quiz.setOnClickListener(this);

        // Set Start OnClick
        start = findViewById(R.id.btn_startGame);
        start.setOnClickListener(this);

        // Set Join OnClick
        join = findViewById(R.id.btn_joinGame);
        join.setOnClickListener(this);
    }

    // Open Activity based on button clicked
    @Override
    public void onClick(View v) {
        String msg;
        switch (v.getId()) {
            case R.id.btn_quiz:
                startActivity(new Intent(MainActivity.this, QuizSelect.class));
                break;
            case R.id.btn_startGame:
                msg = "Sorry. Game Start isnt available yet :)))";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_joinGame:
                msg = "Sorry. Join game isn't avialable yet :)))";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
            case R.id.img_icon:
                msg = "Sorry. Settings don't exist yet :D";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                //FragmentManager fm = getSupportFragmentManager();
                //Fragment fragment = new QuizFragment();
                //fm.beginTransaction().replace(R.id.main_layout,fragment).commit();
                break;
        }
    }

}
