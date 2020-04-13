package com.michael.fakeorfact.game

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView

import com.michael.fakeorfact.R

class CorrectFragment : Fragment() {
    private var aniCorrect: LottieAnimationView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v :View = inflater.inflate(R.layout.fragment_correct, container, false)
        aniCorrect = v.findViewById(R.id.ani_correct)
        aniCorrect!!.playAnimation()

        aniCorrect!!.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?){}
            override fun onAnimationEnd(animation: Animator?){
                parentFragmentManager.popBackStackImmediate()
            }
            override fun onAnimationCancel(animation: Animator?){}
            override fun onAnimationStart(animation: Animator?){}
        })

        return v
    }
}
