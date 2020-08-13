package com.michael.fakeorfact.ui

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.michael.fakeorfact.R
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.misc.BounceInterpolator
import com.michael.fakeorfact.misc.Dialog
import com.michael.fakeorfact.model.QuestionsViewModel
import kotlinx.android.synthetic.main.fragment_quiz.view.*


class Quiz : Fragment() {

    private var loading: LottieAnimationView? = null
    private var dialog = Dialog()
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
    lateinit var qExplain: String
    private var category: String? = null
    lateinit var mAdView : AdView
    private var loop: Int = 0
    private var temp: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_quiz, container, false)
        // Set Up View, Buttons, and TextView
        next = v.btn_next
        explain = v.btn_explain
        wrong = v.btn_fake
        correct = v.btn_fact
        loading = v.ani_loading
        quizQuestion = v.txt_quizQuestion
        txtTimer = v.txt_timer
        imgCategory = v.img_category
        txtQuestion = v.txt_question_count

        txtQuestion!!.visibility = View.GONE

        correct!!.setOnClickListener { answerAnimation("Fact") }
        wrong!!.setOnClickListener { answerAnimation("Fake") }
        next!!.setOnClickListener { nextQuestion() }
        explain!!.setOnClickListener { dialog.showDialogBox(resources.getString(R.string.explain),
                qExplain, context) }

        MobileAds.initialize(context)
        mAdView = v.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Get Data from database using ViewModel
        questionsViewModel = ViewModelProvider(this).get(QuestionsViewModel::class.java)
        category = arguments?.getString("Michael is great")
        viewQuestions(category!!)

        // Set Image based on chosen category
        when(category){
            "History"-> imgCategory!!.setImageResource(R.drawable.history)
            "Science"-> imgCategory!!.setImageResource(R.drawable.science)
            "Art"-> imgCategory!!.setImageResource(R.drawable.art)
            "Random"-> imgCategory!!.setImageResource(R.drawable.random)
        }
        return v
    }

    // Observes the questions and adapts
    private fun viewQuestions(category: String) {
        if(questionList.isNullOrEmpty()){ hideViews("Start") }
        questionsViewModel.getQuestions(category).observe(viewLifecycleOwner, Observer {
            questionList = it as MutableList<Question>?
            // Shuffle Question List
            questionList!!.shuffle()
            Log.d("Questions: ", "repository got: $questionList")
            nextQuestion()
        })
    }
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
        quizQuestion!!.text = currentQuestion.Question
        qAns = currentQuestion.Answer
        qExplain = currentQuestion.Explain
        temp = hideViews("Next")
        if(temp == 1)
            showGameEnd()
        else{
            showViews()
            correct!!.isClickable = true
            wrong!!.isClickable = true
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
        normalButtons()

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
                wrong!!.visibility = View.GONE
                correct!!.visibility = View.GONE
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
            val myAnim = AnimationUtils.loadAnimation(context, R.anim.expand)
            val interpolator = BounceInterpolator(0.2, 30.0)
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
        val manager = parentFragmentManager
        val transaction = manager.beginTransaction()
        lateinit var fragment: Fragment
        if(choice == "Fact" && qAns == true || choice == "Fake" && qAns == false) {
            fragment = CorrectFragment()
            if(choice == "Fact") {
                correct!!.setBackgroundResource(R.drawable.correct_button)
                wrong!!.background.alpha = 64
            }
            else{
                wrong!!.setBackgroundResource(R.drawable.correct_button)
                correct!!.background.alpha = 64
            }
        }
        else if(choice == "Fact" && qAns == false || choice == "Fake" && qAns == true) {
            fragment = WrongFragment()
            if(choice == "Fact") {
                correct!!.setBackgroundResource(R.drawable.wrong_button)
                wrong!!.background.alpha = 64
            }
            else{
                wrong!!.setBackgroundResource(R.drawable.wrong_button)
                correct!!.background.alpha = 64
            }
        }
        transaction.replace(R.id.quiz_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        next!!.visibility = View.VISIBLE
        explain!!.visibility = View.VISIBLE
    }
    // Set buttons back to normal when "Next" is clicked
    private fun normalButtons(){
        correct!!.setBackgroundResource(R.drawable.button_state)
        wrong!!.setBackgroundResource(R.drawable.button_state)
        wrong!!.background.alpha = 255
        correct!!.background.alpha = 255
    }
    private fun setButtons(){
        correct!!.isClickable = false
        wrong!!.isClickable = false
    }
    // Reach Game End. Display Game Over Screen
    private fun showGameEnd(){
        wrong!!.visibility = View.GONE
        correct!!.visibility = View.GONE
        imgCategory!!.visibility = View.GONE
        txtTimer!!.visibility = View.GONE
        next!!.visibility = View.VISIBLE
        next!!.text = resources.getString(R.string.game_over)
        explain!!.visibility = View.GONE
        quizQuestion!!.visibility = View.VISIBLE
        quizQuestion!!.text = resources.getString(R.string.coming_soon)
        next!!.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_quiz_to_mainFragment)
        }
    }
    override fun onStart() {
        super.onStart()
        // Controls Back Button Key. If pressed and 'Yes' go to Main Menu
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dialog.showLeavingDialogBox(context, this)
                return@OnKeyListener true
            }
            false
        })
    }
}