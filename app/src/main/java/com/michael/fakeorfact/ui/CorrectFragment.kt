package com.michael.fakeorfact.ui

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.LottieAnimationView

import com.michael.fakeorfact.R

class CorrectFragment : Fragment() {
    private var aniCorrect: LottieAnimationView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v :View = inflater.inflate(R.layout.fragment_correct, container, false)
        aniCorrect = v.findViewById(R.id.ani_correct)
        aniCorrect!!.playAnimation()

        val category: String? = arguments?.getString("Michael is great")
        val bundle = Bundle()
        bundle.putString("Michael is great", category)
        aniCorrect!!.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?){}
            override fun onAnimationEnd(animation: Animator?){
                NavHostFragment.findNavController(this@CorrectFragment)
                        .navigate(R.id.action_correctFragment_to_quiz, bundle)
            }
            override fun onAnimationCancel(animation: Animator?){}
            override fun onAnimationStart(animation: Animator?){}
        })

        return v
    }
}
