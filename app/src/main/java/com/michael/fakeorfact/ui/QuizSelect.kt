package com.michael.fakeorfact.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.R
import kotlinx.android.synthetic.main.fragment_quiz_select.view.*

class QuizSelect : Fragment() {

    private var chosen: String = ""
    // Animation Variables
    private val ani = arrayOfNulls<LottieAnimationView>(4)
    private var aniUnfocus: LottieAnimationView? = null
    private val aniID = intArrayOf(R.id.ani_historyCheck, R.id.ani_scienceCheck,
            R.id.ani_artCheck, R.id.ani_randomCheck)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_quiz_select, container, false)
        // Set up History Button On Click
        val history: Button = v.btn_history
        val science: Button = v.btn_science
        val art: Button = v.btn_art
        val random: Button = v.btn_random
        val start: Button = v.btn_start

        history.setOnClickListener { setAnimationFocus(aniUnfocus, ani[0], "History") }
        science.setOnClickListener { setAnimationFocus(aniUnfocus, ani[1], "Science") }
        art.setOnClickListener { setAnimationFocus(aniUnfocus, ani[2], "Art") }
        random.setOnClickListener { setAnimationFocus(aniUnfocus, ani[3], "Random") }
        start.setOnClickListener {
            if (!checkAnimations()){
                Toast.makeText(context, "To start a game, " +
                        "please select a category", Toast.LENGTH_SHORT).show()
            } else{
                // Pass category choice to quiz activity and start it
                val bundle = Bundle()
                bundle.putString("Michael is great", chosen)
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_quizSelect_to_quiz, bundle)
            }
        }

        for (i in ani.indices){
            ani[i] = v.findViewById(aniID[i])
        }
        aniUnfocus = ani[0]
        return v
    }

    // Determine if a category has been selected
    private fun checkAnimations(): Boolean{
        return !(ani[0]!!.progress == 0f && ani[1]!!.progress == 0f && ani[2]!!.progress == 0f &&
                ani[3]!!.progress == 0f)
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