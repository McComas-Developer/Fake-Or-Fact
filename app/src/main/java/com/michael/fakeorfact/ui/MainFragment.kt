package com.michael.fakeorfact.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.michael.fakeorfact.R
import com.michael.fakeorfact.misc.BounceInterpolator
import com.michael.fakeorfact.misc.Dialog
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.adView
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import java.util.*

class MainFragment : Fragment() {
    private var played: Boolean = false         // Track Settings Icon Use
    private lateinit var settings: ImageView    // Setting Icon
    private lateinit var info: ImageButton      // Info Icon
    private lateinit var dark: ImageButton      // Dark Mode Icon
    private lateinit var contact: ImageButton   // contact Icon
    private var dialog = Dialog()
    private lateinit var quiz: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)

        info = v.img_btn_info
        dark = v.img_btn_dark
        contact = v.img_btn_contact
        settings = v.img_icon
        quiz = v.btn_quiz

        MobileAds.initialize(context)
        val adView = v.adView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        hideViews()
        quiz.setOnClickListener {  NavHostFragment.findNavController(this)
                .navigate(R.id.action_mainFragment_to_quizSelect) }
        dark.setOnClickListener { Toast.makeText(context, "Dark Mode Coming Soon",
                Toast.LENGTH_LONG).show() }
        contact.setOnClickListener { dialog.showDialogBox(resources.getString(R.string.contact_title),
                resources.getString(R.string.contact_box_dialog), context) }
        info.setOnClickListener { dialog.showAboutDialog(context, this) }
        settings.setOnClickListener {
            val aniRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)
            settings.startAnimation(aniRotate)
            aniRotate.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(arg0: Animation){ anim() }
                override fun onAnimationRepeat(arg0: Animation){}
                override fun onAnimationEnd(arg0: Animation){}
            })
        }

        MobileAds.initialize(context){}
        val timer = Timer()
        val myAnim = AnimationUtils.loadAnimation(context, R.anim.expand)
        //Set the schedule function
        timer.scheduleAtFixedRate(object : TimerTask(){
            override fun run(){
                // Use bounce interpolator with amplitude 0.2 and frequency 30
                val interpolator = BounceInterpolator(0.2, 30.0)
                myAnim.interpolator = interpolator
                settings.startAnimation(myAnim)
            }
        }, 0, 15000)

        return v
    }
    private fun anim(){
        if (!played){
            played = true
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            info.visibility = View.VISIBLE
            dark.visibility = View.VISIBLE
            contact.visibility = View.VISIBLE
            info.startAnimation(aniFade)
            dark.startAnimation(aniFade)
            contact.startAnimation(aniFade)
        } else{
            played = false
            val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            info.startAnimation(aniFade)
            dark.startAnimation(aniFade)
            contact.startAnimation(aniFade)
            hideViews()
        }
    }
    private fun hideViews(){
        info.visibility = View.GONE
        dark.visibility = View.GONE
        contact.visibility = View.GONE
    }
}