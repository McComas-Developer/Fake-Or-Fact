package com.michael.fakeorfact.game

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.fakeorfact.R
import com.michael.fakeorfact.game.multi.WaitScreen
import com.michael.fakeorfact.model.QuestionsViewModel
import java.util.*

class JoinGame : AppCompatActivity() {
    private var btnJoin: Button? = null
    private var codeName: EditText? = null
    private var code: EditText? = null
    private var playerID: String? = null
    private var progBar: ProgressBar? = null
    private var questionsViewModel: QuestionsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)
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
            }
            else if(code!!.text.toString().trim().isNotEmpty() &&
                    codeName!!.text.toString().trim().isNotEmpty()){
                progBar!!.visibility = View.VISIBLE
                verifyGame()
            }
        }
        codeInfo.setOnClickListener {
            showDialogBox(resources.getString(R.string.game_code_title),
                    resources.getString(R.string.game_code))
        }
        nameInfo.setOnClickListener {
            showDialogBox(resources.getString(R.string.game_name_title),
                    resources.getString(R.string.game_name))
        }
    }
    private fun showDialogBox(mTitle: String, mMsg: String){
        // Set Up Dialog box
        val build = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialV: View = inflater.inflate(R.layout.dialog_view, null)
        build.setView(dialV)
        val close = dialV.findViewById<Button>(R.id.btn_ok)
        val title = dialV.findViewById<TextView>(R.id.txt_dialog_title)
        val msg = dialV.findViewById<TextView>(R.id.txt_dialog)
        title.text = mTitle
        msg.text = mMsg
        val box: AlertDialog = build.create()
        box.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        close.setOnClickListener { box.dismiss() }
        box.show()
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