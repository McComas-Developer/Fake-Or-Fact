package com.michael.fakeorfact.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.R
import com.michael.fakeorfact.model.QuestionsViewModel
import java.util.*

class StartGame : AppCompatActivity(), View.OnClickListener {
    private var chosen: String? = null
    private var codeName: EditText? = null
    private var progStart: ProgressBar? = null
    private var questionsViewModel: QuestionsViewModel? = null
    private var aniUnfocus: LottieAnimationView? = null
    private val ani = arrayOfNulls<LottieAnimationView>(4)
    private val aniID = intArrayOf(R.id.ani_start_historyCheck, R.id.ani_start_scienceCheck,
            R.id.ani_start_artCheck, R.id.ani_start_randomCheck)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)
        questionsViewModel = ViewModelProvider(this).get(QuestionsViewModel::class.java)
        val art: Button = findViewById(R.id.btn_Start_Art)
        val history: Button = findViewById(R.id.btn_Start_History)
        val science: Button = findViewById(R.id.btn_Start_Science)
        val random: Button = findViewById(R.id.btn_Start_Random)
        val start = findViewById<Button>(R.id.btn_start_game)
        progStart = findViewById(R.id.start_progBar)
        codeName = findViewById(R.id.editTxt_start_codeName)
        art.setOnClickListener(this)
        history.setOnClickListener(this)
        science.setOnClickListener(this)
        random.setOnClickListener(this)
        start.setOnClickListener(this)
        for (i in ani.indices) {
            ani[i] = findViewById(aniID[i])
        }
        aniUnfocus = ani[0]
    }
    // Determine option when clicked
    override fun onClick(v: View){
        when (v.id){
            R.id.btn_Start_Art -> setAnimationFocus(aniUnfocus, ani[2], "Art")
            R.id.btn_Start_History -> setAnimationFocus(aniUnfocus, ani[0], "History")
            R.id.btn_Start_Science -> setAnimationFocus(aniUnfocus, ani[1], "Science")
            R.id.btn_Start_Random ->  setAnimationFocus(aniUnfocus, ani[3], "Random")
            R.id.btn_start_game -> {
                if (!checkAnimations()){
                    Toast.makeText(this, "To create a game, " +
                            "please select a category", Toast.LENGTH_SHORT).show()
                } else if (codeName!!.text.toString().trim().isEmpty()) {
                    Toast.makeText(this, "To create a game, " +
                            "please enter a codename", Toast.LENGTH_SHORT).show()
                } else {
                    progStart!!.visibility = View.VISIBLE
                    val code = UUID.randomUUID().toString().substring(0, 7)
                    val playerID = UUID.randomUUID().toString().substring(0, 7)
                    questionsViewModel!!.createGame(code, codeName!!.text.toString(), playerID, this)
                }
            }
        }
    }
    // Determine if a category has been selected
    private fun checkAnimations(): Boolean {
        if (ani[0]!!.progress == 0f) {
            if (ani[1]!!.progress == 0f) {
                if (ani[2]!!.progress == 0f) {
                    if (ani[3]!!.progress == 0f) {
                        return false
                    }
                }
            }
        }
        return true
    }
    // Play selected button animation and reset previously chosen one
    private fun setAnimationFocus(ani_unfocus: LottieAnimationView?, ani_focus: LottieAnimationView?,
                                  choice: String) {
        ani_unfocus!!.progress = 0f
        ani_unfocus.pauseAnimation()
        ani_focus!!.playAnimation()
        chosen = choice
        this.aniUnfocus = ani_focus
    }
}