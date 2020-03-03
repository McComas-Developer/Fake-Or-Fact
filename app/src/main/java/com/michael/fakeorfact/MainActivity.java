package com.michael.fakeorfact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.michael.fakeorfact.game.JoinGame;
import com.michael.fakeorfact.game.QuizSelect;
import com.michael.fakeorfact.game.StartGame;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String ani_played;      // Track Settings Icon Use
    ImageView ic_settings;          // Setting Icon
    ImageButton info;               // Info Icon
    ImageButton dark;           // Question Icon
    ImageButton contact;            // contact Icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ani_played = "False";
        setContentView(R.layout.activity_main);

        // Set Up Settings Icons
        info = findViewById(R.id.img_btn_info);
        dark = findViewById(R.id.img_btn_dark);
        contact = findViewById(R.id.img_btn_contact);
        info.setOnClickListener(this);
        dark.setOnClickListener(this);
        contact.setOnClickListener(this);

        info.setVisibility(View.GONE);
        dark.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);

        // Set Settings OnClick
        ic_settings = findViewById(R.id.img_icon);
        ic_settings.setOnClickListener(this);

        // Set Quiz OnClick
        Button quiz = findViewById(R.id.btn_quiz);
        quiz.setOnClickListener(this);

        // Set Start OnClick
        Button start = findViewById(R.id.btn_startGame);
        start.setOnClickListener(this);

        // Set Join OnClick
        Button join = findViewById(R.id.btn_joinGame);
        join.setOnClickListener(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        Timer timer = new Timer();
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.expand);
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  // Use bounce interpolator with amplitude 0.2 and frequency 20
                  MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                  myAnim.setInterpolator(interpolator);

                  ic_settings.startAnimation(myAnim);
              }
        },0, 20000);
    }

    // Open Activity based on button clicked
    @Override
    public void onClick(View v) {
        String msg;
        // Set Up Dialog box
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        switch (v.getId()) {
            case R.id.btn_quiz:
                ani_played = "False";
                startActivity(new Intent(MainActivity.this, QuizSelect.class));
                info.setVisibility(View.GONE);
                dark.setVisibility(View.GONE);
                contact.setVisibility(View.GONE); break;
            case R.id.btn_startGame:
                startActivity(new Intent(MainActivity.this, StartGame.class));
                break;
            case R.id.btn_joinGame:
                startActivity(new Intent(MainActivity.this, JoinGame.class));
                break;
            case R.id.img_btn_dark:
                msg = "Dark Mode Coming Soon";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); break;
            case R.id.img_btn_contact:
                View dialV = inflater.inflate(R.layout.contact_view,null);
                build.setView(dialV);
                Button close = dialV.findViewById(R.id.btn_contact_ok);

                final AlertDialog box = build.create();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        box.dismiss();
                    }
                });
                box.show(); break;
            case R.id.img_btn_info:
                View dialogV = inflater.inflate(R.layout.info_view,null);
                build.setView(dialogV);
                Button closeBtn = dialogV.findViewById(R.id.btn_info_ok);

                final AlertDialog dialog = build.create();
                closeBtn .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.img_icon:
                Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rotate_clockwise);
                ic_settings.startAnimation(aniRotate);

                aniRotate.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation arg0) {anim();}
                    @Override
                    public void onAnimationRepeat(Animation arg0) {}
                    @Override
                    public void onAnimationEnd(Animation arg0) {}
                }); break;
        }
    }
    public void anim(){
        if(ani_played == "False") {
            ani_played = "True";
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

            info.setVisibility(View.VISIBLE);
            dark.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);

            info.startAnimation(aniFade);
            dark.startAnimation(aniFade);
            contact.startAnimation(aniFade);
        }
        else{
            ani_played = "False";
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

            info.startAnimation(aniFade);
            dark.startAnimation(aniFade);
            contact.startAnimation(aniFade);

            info.setVisibility(View.GONE);
            dark.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }
    }

}