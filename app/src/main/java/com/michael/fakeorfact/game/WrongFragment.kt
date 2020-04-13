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

class WrongFragment : Fragment() {
    private var aniWrong: LottieAnimationView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v :View = inflater.inflate(R.layout.fragment_wrong, container, false)
        aniWrong = v.findViewById(R.id.ani_wrong)
        aniWrong!!.playAnimation()

        aniWrong!!.addAnimatorListener(object: Animator.AnimatorListener {
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
