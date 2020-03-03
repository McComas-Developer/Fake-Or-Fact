package com.michael.fakeorfact

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.michael.fakeorfact.model.QuestionsViewModel

class WaitScreen : AppCompatActivity() {

    private var txtCode: TextView? = null
    private var questionsViewModel: QuestionsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_screen)

        txtCode = findViewById(R.id.txt_code)

        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)

        val intent: Intent = getIntent()
        val code: String = intent.getStringExtra("code") ?: "Error"
        txtCode!!.text = code
    }
    // Controls Back Button Key. If pressed and 'Yes' go to Main Menu
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(this.javaClass.name, "back button pressed")
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {        // Yes button clicked
                        questionsViewModel!!.deleteGame(txtCode!!.text.toString())
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
            builder.setMessage("Are you sure you want to leave the game?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
        }
        return super.onKeyDown(keyCode, event)
    }
}
