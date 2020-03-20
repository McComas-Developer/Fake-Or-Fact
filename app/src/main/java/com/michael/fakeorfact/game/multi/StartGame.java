package com.michael.fakeorfact.game.multi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.michael.fakeorfact.R;
import com.michael.fakeorfact.game.multi.WaitScreen;
import com.michael.fakeorfact.model.QuestionsViewModel;

import java.util.UUID;

public class StartGame extends AppCompatActivity implements View.OnClickListener {
    private String chosen;
    private Button start;
    private String code;
    private String playerID;
    private EditText codeName;
    private ProgressBar progStart;
    private QuestionsViewModel questionsViewModel;
    private LottieAnimationView ani_unfocus;
    private LottieAnimationView[] ani = new LottieAnimationView[4];
    private int[] ani_id = {R.id.ani_start_historyCheck, R.id.ani_start_scienceCheck,
            R.id.ani_start_artCheck, R.id.ani_start_randomCheck};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Button art;
        Button history;
        Button science;
        Button random;

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);

        art = findViewById(R.id.btn_Start_Art);
        history = findViewById(R.id.btn_Start_History);
        science = findViewById(R.id.btn_Start_Science);
        random = findViewById(R.id.btn_Start_Random);
        start = findViewById(R.id.btn_start_game);
        progStart = findViewById(R.id.start_progBar);
        codeName = findViewById(R.id.editTxt_start_codeName);

        art.setOnClickListener(this);
        history.setOnClickListener(this);
        science.setOnClickListener(this);
        random.setOnClickListener(this);
        start.setOnClickListener(this);

        for(int i = 0; i < ani.length; i++){
            ani[i] = findViewById(ani_id[i]);
        }
        ani_unfocus = ani[0];
    }
    public void onClick(View v){
        if(v.getId() == R.id.btn_Start_Art){
            SetAnimationFocus(ani_unfocus, ani[2], "Art");
        }
        else if(v.getId() == R.id.btn_Start_History){
            SetAnimationFocus(ani_unfocus, ani[0], "History");
        }
        else if(v.getId() == R.id.btn_Start_Science){
            SetAnimationFocus(ani_unfocus, ani[1], "Science");
        }
        else if(v.getId() == R.id.btn_Start_Random){
            SetAnimationFocus(ani_unfocus, ani[3], "Random");
        }
        else if(v.getId() == R.id.btn_start_game){
            if(!checkAnimations()){
                Toast.makeText(this, "To create a game, " +
                        "please select a category", Toast.LENGTH_SHORT).show();
            }
            else if(codeName.getText().toString().matches("")){
                Toast.makeText(this, "To create a game, " +
                        "please enter a codename", Toast.LENGTH_SHORT).show();
            }
            else{
                progStart.setVisibility(View.VISIBLE);
                code = UUID.randomUUID().toString().substring(0, 7);
                playerID = UUID.randomUUID().toString().substring(0, 7);
                questionsViewModel.createGame(code, codeName.getText().toString(), playerID);
                Intent i = new Intent(this, WaitScreen.class);
                i.putExtra("code", code);
                i.putExtra("ID", playerID);
                startActivity(i);
            }
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
        return true;
    }
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