package com.michael.fakeorfact

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class Egg : AppCompatActivity() {
    var confetti: LottieAnimationView? = null
    var congrats: TextView? = null
    private var image: ImageView? = null
    var eggBtn: Button? = null
    var count: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg)
        // Database method to grab images store in Firebase Storage
        /*val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("cats")*/
        image = findViewById(R.id.img_easterEgg)
        congrats = findViewById(R.id.txt_eggCongrats)
        eggBtn = findViewById(R.id.btn_easterEgg)
        confetti = findViewById(R.id.ani_eggConfetti)
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
        val myAnim = AnimationUtils.loadAnimation(this, R.anim.expand)
        val interpolator = BounceInterpolator(0.2, 30.0)
        myAnim.interpolator = interpolator
        congrats!!.startAnimation(myAnim)
        // Change Cat based on count
        eggBtn!!.setOnClickListener {
            Toast.makeText(this, "It's a kitty!", Toast.LENGTH_LONG).show()
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
            else
                count += 1
        }
    }
}
