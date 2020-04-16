package com.michael.fakeorfact.game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.R

class QuizSelect : AppCompatActivity(), View.OnClickListener {
    private var chosen: String? = null
    private var history: Button? = null                 // History Category Button
    private var science: Button? = null                 // Science Category Button
    private var art: Button? = null                     // Art Category Button
    private var random: Button? = null                  // Random Category Button
    private var start: Button? = null                   // Start Game Button
    // Animation Variables
    private val ani = arrayOfNulls<LottieAnimationView>(4)
    private var aniUnfocus: LottieAnimationView? = null
    private val aniID = intArrayOf(R.id.ani_historyCheck, R.id.ani_scienceCheck,
            R.id.ani_artCheck, R.id.ani_randomCheck)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_select)
        // Set up History Button On Click
        history = findViewById(R.id.btn_history)
        history!!.setOnClickListener(this)

        // Set up Science Button On Click
        science = findViewById(R.id.btn_science)
        science!!.setOnClickListener(this)

        // Set up Art Button On Click
        art = findViewById(R.id.btn_art)
        art!!.setOnClickListener(this)

        // Set up Random Button On Click
        random = findViewById(R.id.btn_random)
        random!!.setOnClickListener(this)

        // Set up Start Button On Click
        start = findViewById(R.id.btn_start)
        start!!.setOnClickListener(this)
        for (i in ani.indices){
            ani[i] = findViewById(aniID[i])
        }
        aniUnfocus = ani[0]
    }
    // Decide Operation based on Button Click
    override fun onClick(v: View){
        when (v.id) {
            R.id.btn_history -> setAnimationFocus(aniUnfocus, ani[0], "History")
            R.id.btn_science -> setAnimationFocus(aniUnfocus, ani[1], "Science")
            R.id.btn_art -> setAnimationFocus(aniUnfocus, ani[2], "Art")
            R.id.btn_random -> setAnimationFocus(aniUnfocus, ani[3], "Random")
            R.id.btn_start -> {               // If no button selected, inform user to select category
                if (!checkAnimations()){
                    Toast.makeText(this, "To start a game, " +
                            "please select a category", Toast.LENGTH_SHORT).show()
                } else{
                    // Pass category choice to quiz activity and start it
                    val i = Intent(this, QuizActivity::class.java)
                    i.putExtra("choice", chosen)
                    startActivity(i)
                }
            }
        }
    }
    // Determine if a category has been selected
    private fun checkAnimations(): Boolean{
        if (ani[0]!!.progress == 0f){
            if (ani[1]!!.progress == 0f){
                if (ani[2]!!.progress == 0f){
                    if (ani[3]!!.progress == 0f){
                        return false
                    }
                }
            }
        }
        return true
    }
    // Play selected button animation and reset previously chosen one
    private fun setAnimationFocus(unfocus: LottieAnimationView?, focus: LottieAnimationView?,
                                  choice: String){
        unfocus!!.progress = 0f
        unfocus.pauseAnimation()
        focus!!.playAnimation()
        chosen = choice
        this.aniUnfocus = focus
    }
}