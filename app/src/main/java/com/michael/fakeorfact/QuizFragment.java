package com.michael.fakeorfact;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

/** Fragment That Displays Quiz Questions */
public class QuizFragment extends Fragment implements View.OnClickListener{

    View view;                                  // Create View
    TextView quizQuestion;                      // Var for Question TextView
    Button wrong;                               // Var for False Button
    Button correct;                             // Var for True Button

    FirebaseDb db;                              // Create object for database to grab info
    private int qCnt;
    private String qAns;
    private Map<String, String> qSet;           // Current Question Map
    private List<Map<String, String>> qList;    // Store questions


    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set Up View, Buttons, and TextView
        view = inflater.inflate(R.layout.fragment_quiz, container, false);
        wrong = view.findViewById(R.id.btn_fake);
        correct = view.findViewById(R.id.btn_fact);
        quizQuestion = view.findViewById(R.id.txt_quizQuestion);
        wrong.setOnClickListener(this);
        correct.setOnClickListener(this);

        setUpQuestions();
        setUpGame();

        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    // Call Animation on click
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fact: questionAnimation(); break;
            case R.id.btn_fake: questionAnimation(); break;
        }
    }

    // Grab DB Questions And Set Up Controls
    public void setUpGame(){
        // Iterate through Map and grab question and answer
        qSet = qList.get(qCnt);
        for (Map.Entry<String, String> entry : qSet.entrySet()) {
            quizQuestion.setText(entry.getKey());
            qAns = entry.getValue();
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

    public void setUpQuestions(){
        qList = db.getQuestionList();
        // Shuffle List
        Collections.shuffle(qList);
        // Have counter to keep track of questions
        qCnt++;
    }

    // Based on Answer, Decide click outcome
    public void questionAnimation(){
        if (qAns == "True"){
            Toast.makeText(getActivity(), "You got it right!", Toast.LENGTH_SHORT).show();
            // Play Correct Animation
        }
        else if (qAns == "False"){
            Toast.makeText(getActivity(), "Oops! You go it wrong", Toast.LENGTH_SHORT).show();
            // Play InCorrect Animation
        }
    }
}
