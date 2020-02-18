package com.michael.fakeorfact.game

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.model.QuestionsViewModel
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private var loading: LottieAnimationView? = null
    private var quizQuestion: TextView? = null
    private var wrong: Button? = null
    private var correct: Button? = null
    private var next: Button? = null

    var questionList: List<Question>? = null
    private lateinit var currentQuestion: Question
    private lateinit var questionsViewModel: QuestionsViewModel

    private var qAns: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        // Set Up View, Buttons, and TextView
        next = findViewById(R.id.btn_next)
        wrong = findViewById(R.id.btn_fake)
        correct = findViewById(R.id.btn_fact)
        loading = findViewById(R.id.ani_loading)
        quizQuestion = findViewById(R.id.txt_quizQuestion)
        wrong!!.setOnClickListener(this)
        correct!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
        next!!.visibility = View.GONE
        loading!!.visibility = View.GONE
        // Trying new DB way
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)

        val intent: Intent = getIntent()
        val category: String = intent.getStringExtra("choice") ?: "Q"   // If value null, Q
        viewQuestions(category)
    }

    // Call Animation on click
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_fact -> questionAnimation("Fact")
            R.id.btn_fake -> questionAnimation("Fake")
            R.id.btn_next -> nextQuestion()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(this.javaClass.name, "back button pressed")
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        //Yes button clicked
                        val intent = Intent(this@QuizActivity,
                                MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_NEGATIVE ->  //No button clicked
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

    // Set Up Controls
    private fun setUpGame() {
        txt_quizQuestion.text = currentQuestion.Question
        qAns = currentQuestion.Answer
    }

    // Observes the questions and adapts
    private fun viewQuestions(category: String) {
        questionsViewModel.getQuestions(category).observe(this, Observer {
            questionList = it
            nextQuestion()
        })
    }

    //TODO: Set up error handling in case of DB issue. Inform user of problem and quit to MainActivity
// Set-up next question
    private fun nextQuestion() { //Hide views while loading next question
        if(questionsViewModel.currentQuestionIndex >= questionList?.size ?: 0) {
            questionsViewModel.currentQuestionIndex = 0
        }

        wrong!!.visibility = View.GONE
        correct!!.visibility = View.GONE
        quizQuestion!!.visibility = View.GONE
        // Play loading animation and Commence set-up
        loading!!.visibility = View.VISIBLE
        loading!!.playAnimation()

        // Reset loading animation and hide it
        loading!!.progress = 0f
        loading!!.pauseAnimation()
        loading!!.visibility = View.GONE
        // Bring buttons and text back
        wrong!!.visibility = View.VISIBLE
        correct!!.visibility = View.VISIBLE
        quizQuestion!!.visibility = View.VISIBLE
        correct!!.isClickable = true
        wrong!!.isClickable = true
        next!!.visibility = View.GONE

        currentQuestion = questionList?.get(questionsViewModel.currentQuestionIndex++)  ?: return
        setUpGame()
    }

    //TODO: Set up correct and incorrect animation functions to be played upon click
// Based on Answer, Decide click outcome
    private fun questionAnimation(choice: String?) {
        when (choice) {
            "Fact" -> {
                if (qAns == true) { // Play Correct Animation
                    Toast.makeText(this, "You got it right!",
                            Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Oops! You got it wrong",
                            Toast.LENGTH_SHORT).show()
                    // Play InCorrect Animation
                }
                setButtons()
            }
            "Fake" -> {
                if (qAns == false) { // Play Correct Animation
                    Toast.makeText(this, "You got it right!",
                            Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Oops! You got it wrong",
                            Toast.LENGTH_SHORT).show()
                    // Play InCorrect Animation
                }
                setButtons()
            }
        }
    }

    private fun setButtons() {
        correct!!.isClickable = false
        wrong!!.isClickable = false
        next!!.visibility = View.VISIBLE
    }
}