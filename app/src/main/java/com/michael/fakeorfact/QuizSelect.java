package com.michael.fakeorfact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.michael.fakeorfact.db.FirebaseDb;

public class QuizSelect extends AppCompatActivity implements View.OnClickListener {

    private String chosen;
    FirebaseDb myDb = new FirebaseDb();

    // Animation Variables
    private LottieAnimationView[] ani = new LottieAnimationView[4];
    private LottieAnimationView ani_unfocus;
    private int[] ani_id = {R.id.ani_historyCheck, R.id.ani_scienceCheck,
                            R.id.ani_artCheck, R.id.ani_randomCheck};

    Button history;             // History Category Button
    Button science;             // Science Category Button
    Button art;                 // Art Category Button
    Button random;              // Random Category Button
    Button start;               // Start Game Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_select);
        // Set up History Button On Click
        history = findViewById(R.id.btn_history);
        history.setOnClickListener(this);

        // Set up Science Button On Click
        science = findViewById(R.id.btn_science);
        science.setOnClickListener(this);

        // Set up Art Button On Click
        art = findViewById(R.id.btn_art);
        art.setOnClickListener(this);

        // Set up Random Button On Click
        random = findViewById(R.id.btn_random);
        random.setOnClickListener(this);

        // Set up Start Button On Click
        start = findViewById(R.id.btn_start);
        start.setOnClickListener(this);

        for(int i = 0; i < ani.length; i++){
            ani[i] = findViewById(ani_id[i]);
        }
        ani_unfocus = ani[0];
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_history:
                SetAnimationFocus(ani_unfocus, ani[0], "History"); break;
            case R.id.btn_science:
                SetAnimationFocus(ani_unfocus, ani[1], "Science"); break;
            case R.id.btn_art:
                SetAnimationFocus(ani_unfocus, ani[2], "Art"); break;
            case R.id.btn_random:
                SetAnimationFocus(ani_unfocus, ani[3], "Random"); break;
            case R.id.btn_start:
                // If no button selected, inform user to select category
                if(!checkAnimations()){
                    Toast.makeText(this, "To start a game, " +
                            "please select a category", Toast.LENGTH_SHORT).show();
                }
                else{
                    myDb.getQuestions(chosen);
                    // Open Quiz Fragment
                    startActivity(new Intent(QuizSelect.this, QuizActivity.class));
                }
                break;
        }
    }

    // Determine if a category has been selected
    public boolean checkAnimations(){
        if(ani[0].getProgress() == 0) {
            if (ani[1].getProgress() == 0) {
                if (ani[2].getProgress() == 0) {
                    if (ani[3].getProgress() == 0) {
                        return false;
                    }}}}
        return true;}

    // Play selected button animation and reset previously chosen one
    private void SetAnimationFocus(LottieAnimationView ani_unfocus, LottieAnimationView ani_focus,
                String choice){
        ani_unfocus.setProgress(0);
        ani_unfocus.pauseAnimation();
        ani_focus.playAnimation();
        chosen = choice;
        this.ani_unfocus = ani_focus;
    }
}
