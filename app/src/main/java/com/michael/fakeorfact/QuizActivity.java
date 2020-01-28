package com.michael.fakeorfact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.fakeorfact.db.FirebaseDb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    TextView quizQuestion;                      // Var for Question TextView
    Button wrong;                               // Var for False Button
    Button correct;                             // Var for True Button

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
        wrong = findViewById(R.id.btn_fake);
        correct = findViewById(R.id.btn_fact);
        quizQuestion = findViewById(R.id.txt_quizQuestion);
        wrong.setOnClickListener(this);
        correct.setOnClickListener(this);

        setUpQuestions();
        //setUpGame();
    }

    @Override
    // Call Animation on click
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fact: questionAnimation("Fact"); break;
            case R.id.btn_fake: questionAnimation("Fake"); break;
        }
    }

    // Grab DB Questions And Set Up Controls
    public void setUpGame(){
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
        Log.v("Quiz", "grabbed: " + qList);
        // Shuffle List
        Collections.shuffle(qList);
        // Have counter to keep track of questions
        qCnt++;
    }

    // Based on Answer, Decide click outcome
    public void questionAnimation(String choice){
        switch (choice){
            case "Fact":
                if(qAns == "True"){
                // Play Correct Animation
                Toast.makeText(this, "You got it right!", Toast.LENGTH_SHORT).show();}
                else{
                    Toast.makeText(this, "Oops! You got it wrong", Toast.LENGTH_SHORT).show();
                    // Play InCorrect Animation
                }break;
            case "Fake":
                if(qAns == "False"){
                    // Play Correct Animation
                    Toast.makeText(this, "You got it right!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Oops! You got it wrong", Toast.LENGTH_SHORT).show();
                    // Play InCorrect Animation
                }break;
        }
    }
}
