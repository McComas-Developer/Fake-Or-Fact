package com.michael.fakeorfact.game

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.model.QuestionsViewModel
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlin.concurrent.thread


class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private var loading: LottieAnimationView? = null
    private var aniWrong: LottieAnimationView? = null
    private var aniCorrect: LottieAnimationView? = null
    private var quizQuestion: TextView? = null
    private var imgCategory: ImageView? = null
    private var txtWrong: TextView? = null
    private var txtCorrect: TextView? = null
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

        aniWrong = findViewById(R.id.ani_wrong)
        aniCorrect = findViewById(R.id.ani_correct)
        txtWrong = findViewById(R.id.txt_wrong)
        txtCorrect = findViewById(R.id.txt_correct)
        imgCategory = findViewById(R.id.img_category)

        wrong!!.setOnClickListener(this)
        correct!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
        next!!.visibility = View.GONE
        aniWrong!!.visibility = View.GONE
        aniCorrect!!.visibility = View.GONE
        txtWrong!!.visibility = View.GONE
        txtCorrect!!.visibility = View.GONE

        // Trying new DB way
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)
        val intent: Intent = getIntent()
        val category: String = intent.getStringExtra("choice") ?: "Q"   // If value null, Q
        viewQuestions(category)
        // Set Image based on chosen category
        when(category){
            "History"-> imgCategory!!.setImageResource(R.drawable.history)
            "Science"-> imgCategory!!.setImageResource(R.drawable.science)
            "Art"-> imgCategory!!.setImageResource(R.drawable.art)
            "Random"-> imgCategory!!.setImageResource(R.drawable.random)
        }
    }

    // Call Animation on click
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_fact -> questionAnimation("Fact")
            R.id.btn_fake -> questionAnimation("Fake")
            R.id.btn_next -> nextQuestion()
        }
    }
    // Controls Back Button Key. If pressed and 'Yes' go to Main Menu
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(this.javaClass.name, "back button pressed")
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {        // Yes button clicked
                        val intent = Intent(this@QuizActivity,
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

    // Observes the questions and adapts
    private fun viewQuestions(category: String) {
        if(questionList.isNullOrEmpty()){ hideViews() }
        questionsViewModel.getQuestions(category).observe(this, Observer {
            questionList = it
            nextQuestion()
        })
    }
    //TODO: Set up error handling in case of DB issue. Inform user of problem and quit to MainActivity
    //Set-up next question
    private fun nextQuestion() { //Hide views while loading next question
        if(questionsViewModel.currentQuestionIndex >= questionList?.size ?: 0) {
            questionsViewModel.currentQuestionIndex = 0
        }
        currentQuestion = questionList?.get(questionsViewModel.currentQuestionIndex++)  ?: return
        setUpGame()
    }
    // Set Up Controls
    private fun setUpGame() {
        txt_quizQuestion.text = currentQuestion.Question
        qAns = currentQuestion.Answer
        showViews()
    }
    private fun showViews(){
        // Bring buttons and text back
        wrong!!.visibility = View.VISIBLE
        correct!!.visibility = View.VISIBLE
        quizQuestion!!.visibility = View.VISIBLE
        imgCategory!!.visibility = View.VISIBLE
        correct!!.isClickable = true
        wrong!!.isClickable = true
        next!!.visibility = View.GONE

        // Reset loading animation and hide it
        loading!!.progress = 0f
        loading!!.pauseAnimation()
        loading!!.visibility = View.GONE
    }
    private fun hideViews(){
        // Play loading animation and Commence set-up
        loading!!.visibility = View.VISIBLE
        loading!!.playAnimation()

        wrong!!.visibility = View.GONE
        correct!!.visibility = View.GONE
        quizQuestion!!.visibility = View.GONE
        imgCategory!!.visibility = View.GONE
    }
    //Based on Answer, Decide click outcome
    private fun questionAnimation(choice: String?) {
        if(choice == "Fact" && qAns == true || choice == "Fake" && qAns == false){
            answerAnimation("Correct")      // Play Correct Animation
        }
        else{
            answerAnimation("Wrong")// Play wrong animation
        }
        setButtons()
    }
    private fun answerAnimation(answer: String) {
        wrong!!.visibility = View.GONE
        correct!!.visibility = View.GONE
        quizQuestion!!.visibility = View.GONE
        imgCategory!!.visibility = View.GONE
        if(answer == "Correct") {
            txtCorrect!!.visibility = View.VISIBLE
            aniCorrect!!.visibility = View.VISIBLE
            aniCorrect!!.playAnimation()
        }
        else if(answer == "Wrong"){
            txtWrong!!.visibility = View.VISIBLE
            aniWrong!!.visibility = View.VISIBLE
            aniWrong!!.playAnimation()
        }
        val layout: ConstraintLayout = findViewById(R.id.quiz_layout);
        layout.setBackgroundResource(R.color.black)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.layout_fade_in)
        layout.startAnimation(fadeIn)

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
        thread {
            Thread.sleep(2200)
                runOnUiThread {
                    if(answer == "Correct") {
                        aniCorrect!!.visibility = View.GONE
                        txtCorrect!!.visibility = View.GONE
                    }
                    else if(answer == "Wrong"){
                        txtWrong!!.visibility = View.GONE
                        aniWrong!!.visibility = View.GONE
                    }
                    wrong!!.visibility = View.VISIBLE
                    correct!!.visibility = View.VISIBLE
                    quizQuestion!!.visibility = View.VISIBLE
                    imgCategory!!.visibility = View.VISIBLE
                    next!!.visibility = View.VISIBLE
                    layout.setBackgroundResource(R.drawable.background)
                }
        }
    }
    private fun setButtons() {
        correct!!.isClickable = false
        wrong!!.isClickable = false
    }
}