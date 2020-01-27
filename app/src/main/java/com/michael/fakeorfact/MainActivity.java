package com.michael.fakeorfact;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String ani_played;      // Track Settings Icon Use
    ImageView ic_settings;          // Setting Icon
    ImageButton info;               // Info Icon
    ImageButton question;           // Question Icon
    ImageButton contact;            // contact Icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button quiz;                // Quiz Button
        Button start;               // Start Game Button
        Button join;                // Join Game Button

        ani_played = "False";

        setContentView(R.layout.activity_main);

        info = findViewById(R.id.img_btn_info);
        question = findViewById(R.id.img_btn_question);
        contact = findViewById(R.id.img_btn_contact);

        info.setVisibility(View.GONE);
        question.setVisibility(View.GONE);
        contact.setVisibility(View.GONE);

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
                //msg = "Sorry. Settings don't exist yet :D";
                //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

                Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
                ic_settings.startAnimation(aniRotate);

                aniRotate.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation arg0) {anim();}
                    @Override
                    public void onAnimationRepeat(Animation arg0) {}
                    @Override
                    public void onAnimationEnd(Animation arg0) {}
                });
                break;
        }
    }
    public void anim(){
        /*Display display = getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        final int width = point.x; // screen width
        final float halfW = width/1.3f; // roughly screen width with icon
        ObjectAnimator lftToRgt,rgtToLft; // global to class

        // Create Transitions along X-axis
        lftToRgt = ObjectAnimator.ofFloat( ic_settings,"translationX",0f,halfW )
                .setDuration(100);                                  // to animate left to right
        rgtToLft = ObjectAnimator.ofFloat( ic_settings,"translationX",halfW,0f )
                .setDuration(100);                                  // to animate right to left

        AnimatorSet s = new AnimatorSet();

        */if(ani_played == "False") {
            //s.play(lftToRgt);
            //s.start();
            ani_played = "True";
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

            info.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);

            info.startAnimation(aniFade);
            question.startAnimation(aniFade);
            contact.startAnimation(aniFade);

        }
        else{
            //s.play(rgtToLft);
            //s.start();
            ani_played = "False";
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

            info.startAnimation(aniFade);
            question.startAnimation(aniFade);
            contact.startAnimation(aniFade);

            info.setVisibility(View.GONE);
            question.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }
    }

}
