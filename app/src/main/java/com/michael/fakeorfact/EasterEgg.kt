package com.michael.fakeorfact

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.michael.fakeorfact.misc.BounceInterpolator
import kotlinx.android.synthetic.main.fragment_easter_egg.view.*

class EasterEgg : Fragment() {
    var confetti: LottieAnimationView? = null
    private var congrats: TextView? = null
    private var image: ImageView? = null
    private var eggBtn: Button? = null
    var count: Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_easter_egg, container, false)
        image = v.img_easterEgg
        congrats = v.txt_eggCongrats
        eggBtn = v.btn_easterEgg
        confetti = v.ani_eggConfetti
        // Confetti Info
        confetti!!.visibility = View.VISIBLE
        confetti!!.playAnimation()
        confetti!!.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?){}
            override fun onAnimationEnd(animation: Animator?){
                confetti!!.visibility = View.GONE
            }
            override fun onAnimationCancel(animation: Animator?){}
            override fun onAnimationStart(animation: Animator?){}
        })
        // Show congrats
        val myAnim = AnimationUtils.loadAnimation(context, R.anim.expand)
        val interpolator = BounceInterpolator(0.2, 30.0)
        myAnim.interpolator = interpolator
        congrats!!.startAnimation(myAnim)
        // Change Cat based on count
        eggBtn!!.setOnClickListener {
            Toast.makeText(context, "It's a kitty!", Toast.LENGTH_LONG).show()
            when(count){
                1-> image!!.setImageResource(R.mipmap.cat_2)
                2-> image!!.setImageResource(R.mipmap.cat_3)
                3-> image!!.setImageResource(R.mipmap.cat_4)
                4-> image!!.setImageResource(R.mipmap.cat_5)
                5-> image!!.setImageResource(R.mipmap.cat_6)
                6-> image!!.setImageResource(R.mipmap.cat_7)
                7-> image!!.setImageResource(R.mipmap.cat_8)
                8-> image!!.setImageResource(R.mipmap.cat_9)
                9-> image!!.setImageResource(R.mipmap.cat_10)
            }
            if(count == 10) {
                image!!.setImageResource(R.mipmap.cat_1)
                count = 1
            }
            else count += 1
        }
        return v
    }
}