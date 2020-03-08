package com.michael.fakeorfact

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michael.fakeorfact.model.QuestionsViewModel

class WaitScreen : AppCompatActivity() {

    private var txtCode: TextView? = null
    private var code: String? = null
    private var playerView: RecyclerView? = null
    private var playerAdapter: PlayerViewAdapter? = null
    private var btnStart: Button? = null
    private var playerList: List<String> = ArrayList()
    private var questionsViewModel: QuestionsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_screen)

        txtCode = findViewById(R.id.txt_code)
        btnStart = findViewById(R.id.btn_begin)
        playerView = findViewById(R.id.player_view)

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)

        val intent: Intent = getIntent()
        code = intent.getStringExtra("code") ?: "Error"
        txtCode!!.text = ("Code: $code")

        playerView?.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = PlayerViewAdapter(playerList)
        }

        questionsViewModel!!.getPlayers(code!!).observe(this, Observer {
            playerList = it
            Log.d("Michael ", playerList.toString())
            playerAdapter = PlayerViewAdapter(playerList)
            questionsViewModel!!.setAdapter(playerAdapter!!, playerView!!)
        })
        /*btn_begin.setOnClickListener{
            playerList = questionsViewModel!!.getPlayers(code)
            txtPlayer1!!.text = playerList[1]
        }*/
    }
    // Controls Back Button Key. If pressed and 'Yes' go to Main Menu
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("WaitScreen", "back button pressed")
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {        // Yes button clicked
                        code?.let { questionsViewModel!!.deleteGame(it) }
                        val intent = Intent(this@WaitScreen,
                                MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->          // No button clicked
                        dialog.dismiss()
                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setMessage("Are you sure you want to leave the game?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
        }
        return super.onKeyDown(keyCode, event)
    }
}
