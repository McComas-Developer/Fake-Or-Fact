package com.michael.fakeorfact.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.michael.fakeorfact.R
import com.michael.fakeorfact.WaitScreen
import com.michael.fakeorfact.model.QuestionsViewModel
import java.util.*

class JoinGame : AppCompatActivity() {
    private var btnJoin: Button? = null
    private var codeName: EditText? = null
    private var code: EditText? = null
    private var questionsViewModel: QuestionsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)
        btnJoin = findViewById(R.id.btn_join_game)
        code = findViewById(R.id.editTxt_gameCode)
        codeName = findViewById(R.id.editTxt_gameName)

        btnJoin!!.setOnClickListener{
            if(codeName!!.text.toString().trim().length == 0){
                Toast.makeText(this, "To Join a game, please enter a Code Name"
                        , Toast.LENGTH_SHORT).show()
            }
            if(code!!.text.toString().trim().length == 0){
                Toast.makeText(this, "To Join a game, please enter a Code"
                        , Toast.LENGTH_SHORT).show()
            }
            else{
                questionsViewModel!!.addPlayer(code!!.text.toString(), codeName!!.text.toString())
                val i = Intent(this, WaitScreen::class.java)
                i.putExtra("code", code!!.text.toString())
                startActivity(i)
            }
        }
    }
}