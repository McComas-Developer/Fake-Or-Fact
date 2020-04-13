package com.michael.fakeorfact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.michael.fakeorfact.game.JoinGame;
import com.michael.fakeorfact.game.QuizSelect;
import com.michael.fakeorfact.game.multi.StartGame;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String ani_played;      // Track Settings Icon Use
    ImageView ic_settings;          // Setting Icon
    ImageButton info;               // Info Icon
    ImageButton dark;               // Question Icon
    ImageButton contact;            // contact Icon
    Button quiz;
    Button join;
    Button start;

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

        hideViews();
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}});
        Timer timer = new Timer();
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.expand);
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  // Use bounce interpolator with amplitude 0.2 and frequency 20
                  MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 30);
                  myAnim.setInterpolator(interpolator);
                  ic_settings.startAnimation(myAnim);
              }
        },0, 15000);
    }
    // Open Activity based on button clicked
    @Override
    public void onClick(View v) {
        String msg;
        switch (v.getId()) {
            case R.id.btn_quiz:
                afterClick();
                startActivity(new Intent(MainActivity.this, QuizSelect.class));
                break;
            case R.id.btn_startGame:
                afterClick();
                Toast.makeText(this, "Multi-player Coming Soon :D", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, StartGame.class));
                break;
            case R.id.btn_joinGame:
                afterClick();
                Toast.makeText(this, "Multi-player Coming Soon :D", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, JoinGame.class));
                break;
            case R.id.img_btn_dark:
                msg = "Dark Mode Coming Soon";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); break;
            case R.id.img_btn_contact:
                showDialogBox(getResources().getString(R.string.contact_title),
                        getResources().getString(R.string.contact_box_dialog));
                break;
            case R.id.img_btn_info:
                showDialogBox(getResources().getString(R.string.info_title),
                        getResources().getString(R.string.info_box_dialog));
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
        // Reset setting icon if leaving activity
        if(v.getId() == R.id.btn_quiz || v.getId() == R.id.btn_startGame || v.getId() == R.id.btn_joinGame){
            ani_played = "False";
            hideViews();
        }
    }
    public void anim(){
        if(ani_played.equals("False")) {
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
            hideViews();
        }
    }
    public void showDialogBox(String mTitle, String mMsg){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogV = inflater.inflate(R.layout.dialog_view,null);
        build.setView(dialogV);
        // Grab dialog box objects and fill them
        Button closeBtn = dialogV.findViewById(R.id.btn_ok);
        TextView title = dialogV.findViewById(R.id.txt_dialog_title);
        TextView msg = dialogV.findViewById(R.id.txt_dialog);
        title.setText(mTitle);
        msg.setText(mMsg);
        // Show dialog box
        final AlertDialog dialog = build.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss();}});
        dialog.show();
    }
    public void hideViews(){
        info.setVisibility(View.GONE);
        dark.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);
    }
    // Deny additional button click after initial click
    public void afterClick(){
        quiz.setClickable(false);
        join.setClickable(false);
        start.setClickable(false);
    }
    // Re-enable buttons when leaving activity
    @Override
    public void onStop() {
        quiz.setClickable(true);
        join.setClickable(true);
        start.setClickable(true);
        super.onStop();
    }
}