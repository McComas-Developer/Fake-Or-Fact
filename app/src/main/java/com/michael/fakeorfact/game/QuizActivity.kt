package com.michael.fakeorfact.game

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.model.QuestionsViewModel
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*
import kotlin.concurrent.thread


class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private var loading: LottieAnimationView? = null
    private var aniWrong: LottieAnimationView? = null
    private var aniCorrect: LottieAnimationView? = null
    private var globTimer: CountDownTimer? = null
    private var quizQuestion: TextView? = null
    private var imgCategory: ImageView? = null
    private var txtWrong: TextView? = null
    private var txtCorrect: TextView? = null
    private var txtQuestion: TextView? = null
    private var txtTimer: TextView? = null
    private var wrong: Button? = null
    private var explain: Button? = null
    private var correct: Button? = null
    private var next: Button? = null

    var questionList: MutableList<Question>? = null
    private lateinit var currentQuestion: Question
    private lateinit var questionsViewModel: QuestionsViewModel

    private var qAns: Boolean? = null
    private var qExplain: String? = null
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        // Set Up View, Buttons, and TextView
        next = findViewById(R.id.btn_next)
        explain = findViewById(R.id.btn_explain)
        wrong = findViewById(R.id.btn_fake)
        correct = findViewById(R.id.btn_fact)
        loading = findViewById(R.id.ani_loading)
        quizQuestion = findViewById(R.id.txt_quizQuestion)

        aniWrong = findViewById(R.id.ani_wrong)
        aniCorrect = findViewById(R.id.ani_correct)
        txtWrong = findViewById(R.id.txt_wrong)
        txtTimer = findViewById(R.id.txt_timer)
        txtCorrect = findViewById(R.id.txt_correct)
        imgCategory = findViewById(R.id.img_category)
        txtQuestion = findViewById(R.id.txt_question_count)

        wrong!!.setOnClickListener(this)
        explain!!.setOnClickListener(this)
        correct!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
        //next!!.visibility = View.GONE
        txtWrong!!.visibility = View.GONE
        txtCorrect!!.visibility = View.GONE
        txtQuestion!!.visibility = View.GONE

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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
            R.id.btn_explain -> {
                // Set Up Dialog box
                val build = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialV: View = inflater.inflate(R.layout.dialog_view, null)
                build.setView(dialV)
                val close = dialV.findViewById<Button>(R.id.btn_ok)
                val title = dialV.findViewById<TextView>(R.id.txt_dialog_title)
                val msg = dialV.findViewById<TextView>(R.id.txt_dialog)
                title.text = resources.getString(R.string.explain)
                msg.text = qExplain
                val box: AlertDialog = build.create()
                box.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                close.setOnClickListener { box.dismiss() }
                box.show()
            }
        }
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
                val intent = Intent(this@QuizActivity, MainActivity::class.java)
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

    // Observes the questions and adapts
    private fun viewQuestions(category: String) {
        if(questionList.isNullOrEmpty()){ hideViews("Start") }
        questionsViewModel.getQuestions(category).observe(this, Observer {
            questionList = it as MutableList<Question>?
            // Shuffle Question List
            questionList!!.shuffle()
            Log.d("Questions: ", "repository got: $questionList")
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
        qExplain = currentQuestion.Explain
        hideViews("Next")
        thread {
            Thread.sleep(2000)
            runOnUiThread { showViews() }
        }
    }
    private fun showViews(){
        // Bring buttons and text back
        showNormal()
        wrong!!.visibility = View.VISIBLE
        correct!!.visibility = View.VISIBLE
        txtQuestion!!.visibility = View.GONE

        // Reset loading animation and hide it
        loading!!.progress = 0f
        loading!!.pauseAnimation()
        loading!!.visibility = View.GONE

        // Set up Game Timer
        txtTimer!!.setTextColor(Color.parseColor("#FFFFFF"))
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if((millisUntilFinished / 1000) >= 10)
                    txtTimer!!.text = ("0:" + (millisUntilFinished / 1000).toString())
                else if((millisUntilFinished / 1000) < 10) {
                    txtTimer!!.text = ("0:0" + (millisUntilFinished / 1000).toString())
                    if ((millisUntilFinished / 1000) <= 5)
                        txtTimer!!.setTextColor(Color.parseColor("#FF0000"))
                }
            }
            override fun onFinish() {
                setButtons()
                next!!.visibility = View.VISIBLE
            }
        }
        timer.start()
        globTimer = timer
    }
    private fun hideViews(option: String){
        // Play loading animation and Commence set-up
        if(option == "Start") {
            loading!!.visibility = View.VISIBLE
            loading!!.playAnimation()
        }
        else if(option == "Next"){
            loading!!.visibility = View.GONE
            txtQuestion!!.text = ("Question " + questionsViewModel.currentQuestionIndex)
            if(questionsViewModel.currentQuestionIndex == 20)
                txtQuestion!!.text = ("Last Question!")
            txtQuestion!!.visibility = View.VISIBLE

            val myAnim = AnimationUtils.loadAnimation(this, R.anim.expand)
            val interpolator = Bounce(0.2, 30.0)
            myAnim.interpolator = interpolator

            txtQuestion!!.startAnimation(myAnim)
        }
        explain!!.visibility = View.GONE
        next!!.visibility = View.GONE
        wrong!!.visibility = View.GONE
        correct!!.visibility = View.GONE
        txtTimer!!.visibility = View.GONE
        quizQuestion!!.visibility = View.GONE
        imgCategory!!.visibility = View.GONE
    }
    //Based on Answer, Decide click outcome
    private fun questionAnimation(choice: String?) {
        if(choice == "Fact" && qAns == true || choice == "Fake" && qAns == false){
            answerAnimation("Correct")      // Play Correct Animation
        }
        else{ answerAnimation("Wrong") }    // Play wrong animation
        setButtons()
    }
    private fun answerAnimation(answer: String) {
        globTimer!!.cancel()
        setButtons()
        mAdView.visibility = View.GONE
        quizQuestion!!.visibility = View.GONE
        imgCategory!!.visibility = View.GONE
        txtTimer!!.visibility = View.GONE
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
                    showNormal()
                    next!!.visibility = View.VISIBLE
                    explain!!.visibility = View.VISIBLE
                    layout.setBackgroundResource(R.drawable.background)
                }
        }
    }
    private fun setButtons() {
        correct!!.visibility = View.GONE
        wrong!!.visibility = View.GONE
    }
    private fun showNormal(){
        mAdView.visibility = View.VISIBLE
        txtTimer!!.visibility = View.VISIBLE
        quizQuestion!!.visibility = View.VISIBLE
        imgCategory!!.visibility = View.VISIBLE
    }
}