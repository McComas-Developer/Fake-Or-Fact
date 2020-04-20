package com.michael.fakeorfact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.michael.fakeorfact.game.multi.JoinGame
import com.michael.fakeorfact.game.QuizSelect
import com.michael.fakeorfact.game.multi.StartGame
import com.michael.fakeorfact.misc.BounceInterpolator
import com.michael.fakeorfact.misc.Dialog
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener{
    private var played: String? = null          // Track Settings Icon Use
    var settings: ImageView? = null             // Setting Icon
    private var info: ImageButton? = null       // Info Icon
    private var dark: ImageButton? = null       // Dark Mode Icon
    private var contact: ImageButton? = null    // contact Icon
    private var dialog = Dialog()
    private var quiz: Button? = null
    private var join: Button? = null
    private var start: Button? = null
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        played = "False"
        setContentView(R.layout.activity_main)

        // Set Up Settings Icons
        info = findViewById(R.id.img_btn_info)
        dark = findViewById(R.id.img_btn_dark)
        contact = findViewById(R.id.img_btn_contact)
        info!!.setOnClickListener(this)
        dark!!.setOnClickListener(this)
        contact!!.setOnClickListener(this)
        hideViews()
        // Set Settings OnClick
        settings = findViewById(R.id.img_icon)
        settings!!.setOnClickListener(this)

        // Set Quiz OnClick
        quiz = findViewById(R.id.btn_quiz)
        quiz!!.setOnClickListener(this)

        // Set Start OnClick
        start = findViewById(R.id.btn_startGame)
        start!!.setOnClickListener(this)

        // Set Join OnClick
        join = findViewById(R.id.btn_joinGame)
        join!!.setOnClickListener(this)
        MobileAds.initialize(this){}
        val timer = Timer()
        val myAnim = AnimationUtils.loadAnimation(this, R.anim.expand)
        //Set the schedule function
        timer.scheduleAtFixedRate(object : TimerTask(){
            override fun run(){
                // Use bounce interpolator with amplitude 0.2 and frequency 30
                val interpolator = BounceInterpolator(0.2, 30.0)
                myAnim.interpolator = interpolator
                settings!!.startAnimation(myAnim)
            }
        }, 0, 15000)
    }
    // Open Activity based on button clicked
    override fun onClick(v: View){
        when (v.id){
            R.id.btn_quiz -> {
                afterClick()
                startActivity(Intent(this@MainActivity, QuizSelect::class.java))
            }
            R.id.btn_startGame -> {                //afterClick();
                Toast.makeText(this, "Multi-player Coming Soon :D", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this@MainActivity, StartGame::class.java))
            }
            R.id.btn_joinGame -> {                //afterClick();
                Toast.makeText(this, "Multi-player Coming Soon :D", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this@MainActivity, JoinGame::class.java))
            }
            R.id.img_btn_dark -> Toast.makeText(this, "Dark Mode Coming Soon", Toast.LENGTH_LONG).show()
            R.id.img_btn_contact -> {
                dialog.showDialogBox(resources.getString(R.string.contact_title),
                        resources.getString(R.string.contact_box_dialog), this@MainActivity)
            }
            R.id.img_btn_info -> dialog.showAboutDialog(this@MainActivity)
            R.id.img_icon -> {
                val aniRotate = AnimationUtils.loadAnimation(applicationContext,
                        R.anim.rotate_clockwise)
                settings!!.startAnimation(aniRotate)
                aniRotate.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationStart(arg0: Animation){ anim() }
                    override fun onAnimationRepeat(arg0: Animation){}
                    override fun onAnimationEnd(arg0: Animation){}
                })
            }
        }
        // Reset setting icon if leaving activity
        if (v.id == R.id.btn_quiz || v.id == R.id.btn_startGame || v.id == R.id.btn_joinGame){
            played = "False"
            hideViews()
        }
    }
    private fun anim(){
        if (played == "False"){
            played = "True"
            val aniFade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
            info!!.visibility = View.VISIBLE
            dark!!.visibility = View.VISIBLE
            contact!!.visibility = View.VISIBLE
            info!!.startAnimation(aniFade)
            dark!!.startAnimation(aniFade)
            contact!!.startAnimation(aniFade)
        } else{
            played = "False"
            val aniFade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
            info!!.startAnimation(aniFade)
            dark!!.startAnimation(aniFade)
            contact!!.startAnimation(aniFade)
            hideViews()
        }
    }
    private fun hideViews(){
        info!!.visibility = View.GONE
        dark!!.visibility = View.GONE
        contact!!.visibility = View.GONE
    }
    // Deny additional button click after initial click
    private fun afterClick(){
        quiz!!.isClickable = false
        join!!.isClickable = false
        start!!.isClickable = false
    }
    // Re-enable buttons when leaving activity
    public override fun onStop(){
        quiz!!.isClickable = true
        join!!.isClickable = true
        start!!.isClickable = true
        super.onStop()
    }
}