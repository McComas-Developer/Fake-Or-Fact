package com.michael.fakeorfact.game.multi

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R
import com.michael.fakeorfact.model.QuestionsViewModel
import com.michael.fakeorfact.player.PlayerViewAdapter

class WaitScreen : AppCompatActivity() {

    private var txtCode: TextView? = null
    private var code: String? = null
    private var playerID: String? = null
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

        // Grab passed options for game: Start game vs. Join Game
        val intent: Intent = getIntent()
        code = intent.getStringExtra("code") ?: "Error"
        playerID = intent.getStringExtra("ID") ?: "Error"
        txtCode!!.text = ("Code: $code")

        // Set recyclerview layout and adapter
        playerView?.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = PlayerViewAdapter(playerList)
        }
        // Set observer for player list
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
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            // Set view for dialog
            val dialV = inflater.inflate(R.layout.leave_view, null)
            builder.setView(dialV)
            // Find buttons in layout
            val yes: Button = dialV.findViewById(R.id.btn_yes)
            val no: Button = dialV.findViewById(R.id.btn_no)
            // Create dialog box and show
            val dialog: AlertDialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.show()
            Log.d(this.javaClass.name, "back button pressed")
            // 'Yes' clicked; exit quiz
            yes.setOnClickListener {
                dialog.dismiss()
                questionsViewModel!!.removePlayer(code!!, playerID!!)
                val intent = Intent(this@WaitScreen, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            // 'No' clicked; dismiss
            no.setOnClickListener {
                dialog.dismiss()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /*override fun onDestroy() {
        // Remove player when app is closed
        questionsViewModel!!.removePlayer(code!!, playerID!!)
        super.onDestroy()
    }*/
}
