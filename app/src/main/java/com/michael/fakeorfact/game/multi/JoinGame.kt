package com.michael.fakeorfact.game.multi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.fakeorfact.misc.Dialog
import com.michael.fakeorfact.misc.InternetCheck
import com.michael.fakeorfact.R
import com.michael.fakeorfact.model.QuestionsViewModel
import java.util.*

class JoinGame : AppCompatActivity() {
    private var dialog = Dialog()
    private var btnJoin: Button? = null
    private var codeName: EditText? = null
    private var code: EditText? = null
    private var playerID: String? = null
    private var progBar: ProgressBar? = null
    private var questionsViewModel: QuestionsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        questionsViewModel = ViewModelProvider(this).get(QuestionsViewModel::class.java)
        btnJoin = findViewById(R.id.btn_join_game)
        code = findViewById(R.id.editTxt_gameCode)
        progBar = findViewById(R.id.join_progBar)
        codeName = findViewById(R.id.editTxt_gameName)
        val codeInfo: ImageButton = findViewById(R.id.img_btn_info)
        val nameInfo: ImageButton = findViewById(R.id.img_btn_info2)

        btnJoin!!.setOnClickListener{
            if(codeName!!.text.toString().trim().isEmpty()){
                Toast.makeText(this, "To Join a game, please enter a Code Name"
                        , Toast.LENGTH_SHORT).show()
            }
            if(code!!.text.toString().trim().isEmpty()){
                Toast.makeText(this, "To Join a game, please enter a Code"
                        , Toast.LENGTH_SHORT).show()
            }// If fields are filled, check internet connection, and join game if it exists
            else if(code!!.text.toString().trim().isNotEmpty() &&
                    codeName!!.text.toString().trim().isNotEmpty()){
                // If fields are filled, but name is too long, notify user and quit
                if(codeName!!.text.toString().trim().length > 25){
                    Toast.makeText(this, "Please enter a Code Name, " +
                            "less than 25 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                progBar!!.visibility = View.VISIBLE
                InternetCheck(object : InternetCheck.Consumer {
                    override fun accept(internet: Boolean?) {
                        if (internet!!)
                            verifyGame()
                        else {
                            dialog.showDialogBox(resources.getString(R.string.wrong_title),
                                    resources.getString(R.string.wrong_dialog), this@JoinGame)
                            progBar!!.visibility = View.GONE
                        }
                    }
                })
            }
        }
        codeInfo.setOnClickListener {
            dialog.showDialogBox(resources.getString(R.string.game_code_title),
                    resources.getString(R.string.game_code), this@JoinGame)
        }
        nameInfo.setOnClickListener {
            dialog.showDialogBox(resources.getString(R.string.game_name_title),
                    resources.getString(R.string.game_name), this@JoinGame)
        }
    }
    private fun verifyGame(){
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Games").document(code!!.text.toString())
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    Log.d("VerifyGame", "Game exists!")
                    playerID = UUID.randomUUID().toString().substring(0, 7)
                    questionsViewModel!!.addPlayer(code!!.text.toString(),
                            codeName!!.text.toString(), playerID!!)
                    val i = Intent(this, WaitScreen::class.java)
                    i.putExtra("code", code!!.text.toString())
                    i.putExtra("ID", playerID)
                    startActivity(i)
                    return@addOnCompleteListener
                }
                else
                    Log.d("VerifyGame", "Game does not exist!")
                    Toast.makeText(this, "Code not found. Please make sure it is " +
                        "typed correctly.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("VerifyGame", "Can't verify existence: ", task.exception)
            }
            progBar!!.visibility = View.GONE
        }
    }
}