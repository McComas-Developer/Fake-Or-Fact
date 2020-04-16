package com.michael.fakeorfact.game

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.michael.fakeorfact.MainActivity
import com.michael.fakeorfact.R
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.model.QuestionsViewModel
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlin.concurrent.thread

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private var loading: LottieAnimationView? = null
    private var globTimer: CountDownTimer? = null
    private var quizQuestion: TextView? = null
    private var imgCategory: ImageView? = null
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
    private var loop: Int = 0
    private var temp: Int = 0

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

        txtTimer = findViewById(R.id.txt_timer)
        imgCategory = findViewById(R.id.img_category)
        txtQuestion = findViewById(R.id.txt_question_count)

        wrong!!.setOnClickListener(this)
        explain!!.setOnClickListener(this)
        correct!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
        txtQuestion!!.visibility = View.GONE

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Get Data from database using ViewModel
        questionsViewModel = ViewModelProvider(this).get(QuestionsViewModel::class.java)
        val intent: Intent = intent
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
            R.id.btn_fact -> answerAnimation("Fact")
            R.id.btn_fake -> answerAnimation("Fake")
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
            val dialV = inflater.inflate(R.layout.leave_view, null)
            builder.setView(dialV)
            val yes: Button = dialV.findViewById(R.id.btn_yes)
            val no: Button = dialV.findViewById(R.id.btn_no)
            val dialog: AlertDialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.show()
            Log.d(this.javaClass.name, "back button pressed")
            setBack(yes)
            no.setOnClickListener { dialog.dismiss() }
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
    private fun nextQuestion(){
        if(questionsViewModel.currentQuestionIndex >= questionList?.size ?: 0) {
            questionsViewModel.currentQuestionIndex = 0
        }
        currentQuestion = questionList?.get(questionsViewModel.currentQuestionIndex++)  ?: return
        setUpGame()
    }
    // Set Up Controls
    private fun setUpGame(){
        txt_quizQuestion.text = currentQuestion.Question
        qAns = currentQuestion.Answer
        qExplain = currentQuestion.Explain
        temp = hideViews("Next")
        if(temp == 1)
            showGameEnd()
        else{
            thread {
                Thread.sleep(2000)
                runOnUiThread { showViews() }
            }
        }
    }
    private fun showViews(){
        // Bring buttons and text back
        mAdView.visibility = View.VISIBLE
        txtTimer!!.visibility = View.VISIBLE
        quizQuestion!!.visibility = View.VISIBLE
        imgCategory!!.visibility = View.VISIBLE
        wrong!!.visibility = View.VISIBLE
        correct!!.visibility = View.VISIBLE
        txtQuestion!!.visibility = View.GONE
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
            override fun onFinish(){
                setButtons()
                next!!.visibility = View.VISIBLE
            }
        }
        timer.start()
        globTimer = timer
    }
    private fun hideViews(option: String): Int {
        // Play loading animation and Commence set-up
        if(option == "Start"){
            loading!!.visibility = View.VISIBLE
            loading!!.playAnimation()
        }
        else if(option == "Next"){
            loading!!.visibility = View.GONE
            txtQuestion!!.text = ("Question " + questionsViewModel.currentQuestionIndex)
            if(questionsViewModel.currentQuestionIndex == 20) {
                txtQuestion!!.text = ("Last Question!")
                loop = 1
            }
            else if(loop == 1){
                txtQuestion!!.text = ("Game Over")
                loop = 2
            }
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
        if(loop == 2)
            return 1
        return 0
    }
    //Based on Answer, Decide click outcome
    private fun answerAnimation(choice: String){
        globTimer!!.cancel()
        setButtons()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        if(choice == "Fact" && qAns == true || choice == "Fake" && qAns == false) {
            val correctFragment = CorrectFragment()
            transaction.replace(R.id.quiz_fragment, correctFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        else{
            val correctFragment = WrongFragment()
            transaction.replace(R.id.quiz_fragment, correctFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        next!!.visibility = View.VISIBLE
        explain!!.visibility = View.VISIBLE
    }
    private fun setButtons(){
        correct!!.visibility = View.GONE
        wrong!!.visibility = View.GONE
    }
    private fun setBack(btn: Button){
        btn.setOnClickListener {
            val intent = Intent(this@QuizActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
    private fun showGameEnd(){
        setButtons()
        imgCategory!!.visibility = View.GONE
        txtTimer!!.visibility = View.GONE
        next!!.visibility = View.VISIBLE
        next!!.text = "Go to Main Menu"
        explain!!.visibility = View.GONE
        quizQuestion!!.visibility = View.VISIBLE
        quizQuestion!!.text = "You have completed all questions for this category, but more are coming soon!"
        setBack(next!!)
    }
}