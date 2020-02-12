package com.michael.fakeorfact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.michael.fakeorfact.db.FirebaseDb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO: Need to resolve database crash when trying to sort through data
    LottieAnimationView loading;
    TextView quizQuestion;                      // Var for Question TextView
    Button wrong;                               // Var for False Button
    Button correct;                             // Var for True Button
    Button next;

    //FirebaseDb db;                              // Create object for database to grab info
    private int qCnt;
    private String qAns;
    //private Map<String, String> qSet;           // Current Question Map
    private List<Map<String, String>> qList;    // Store questions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Set Up View, Buttons, and TextView
        next = findViewById(R.id.btn_next);
        wrong = findViewById(R.id.btn_fake);
        correct = findViewById(R.id.btn_fact);
        loading = findViewById(R.id.ani_loading);
        quizQuestion = findViewById(R.id.txt_quizQuestion);

        wrong.setOnClickListener(this);
        correct.setOnClickListener(this);
        next.setOnClickListener(this);

        next.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        //setUpQuestions();
        //setUpGame();
    }

    @Override
    // Call Animation on click
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fact: questionAnimation("Fact"); break;
            case R.id.btn_fake: questionAnimation("Fake"); break;
            case R.id.btn_next: nextQuestion(); break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent intent = new Intent(QuizActivity.this,
                                    MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to leave the game?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    // Grab DB Questions And Set Up Controls
    public void setUpGame(){
        Log.d("Elijah", "Q list == null?????  :  " +(qList == null));
        Map<String, String> qSet = qList.get(qCnt);
        // Iterate through Map and grab question and answer
        //qSet = qList.get(qCnt);
        for (Map.Entry<String, String> entry : qSet.entrySet()) {
            quizQuestion.setText(entry.getKey());
            qAns = entry.getValue();
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

    public void setUpQuestions(){
        FirebaseDb db = new FirebaseDb();
        db.getQuestions("dummy");
        qList = db.getQuestionList();
        Log.d("Elijah", "Q list == null immediately after db call  :  " +(qList == null));

        Log.v("Quiz", "grabbed: " + qList);
        System.out.println(qList);
        // Shuffle List
        //Collections.shuffle(qList);
        // Have counter to keep track of questions
        qCnt++;
    }
//TODO: Set up error handling in case of DB issue. Inform user of problem and quit to MainActivity
    // Set-up next question
    public void nextQuestion(){
        //Hide views while loading next question
        wrong.setVisibility(View.GONE);
        correct.setVisibility(View.GONE);
        quizQuestion.setVisibility(View.GONE);
        // Play loading animation and Commence set-up
        loading.setVisibility(View.VISIBLE);
        loading.playAnimation();
        setUpQuestions();
        setUpGame();
        // Reset loading animation and hide it
        loading.setProgress(0);
        loading.pauseAnimation();
        loading.setVisibility(View.GONE);
        // Bring buttons and text back
        wrong.setVisibility(View.VISIBLE);
        correct.setVisibility(View.VISIBLE);
        quizQuestion.setVisibility(View.VISIBLE);
        correct.setClickable(true);
        wrong.setClickable(true);
        next.setVisibility(View.GONE);
    }
//TODO: Set up correct and incorrect animation functions to be played upon click
    // Based on Answer, Decide click outcome
    public void questionAnimation(String choice){
        switch (choice){
            case "Fact":
                if(qAns == "True"){
                // Play Correct Animation
                Toast.makeText(this, "You got it right!",
                        Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(this, "Oops! You got it wrong",
                            Toast.LENGTH_SHORT).show();
                    // Play InCorrect Animation
                } setButtons(); break;
            case "Fake":
                if(qAns == "False"){
                    // Play Correct Animation
                    Toast.makeText(this, "You got it right!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Oops! You got it wrong",
                            Toast.LENGTH_SHORT).show();
                    // Play InCorrect Animation
                } setButtons(); break;
        }
    }
    public void setButtons(){
        correct.setClickable(false);
        wrong.setClickable(false);
        next.setVisibility(View.VISIBLE);
    }
}