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

class WrongFragment : Fragment() {
    private var aniWrong: LottieAnimationView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v :View = inflater.inflate(R.layout.fragment_wrong, container, false)
        aniWrong = v.findViewById(R.id.ani_wrong)
        aniWrong!!.playAnimation()

        val category: String? = arguments?.getString("Michael is great")
        val bundle = Bundle()
        bundle.putString("Michael is great", category)
        aniWrong!!.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?){}
            override fun onAnimationEnd(animation: Animator?){
                NavHostFragment.findNavController(this@WrongFragment)
                        .navigate(R.id.action_wrongFragment_to_quiz, bundle)
            }
            override fun onAnimationCancel(animation: Animator?){}
            override fun onAnimationStart(animation: Animator?){}
        })
        return v
    }
}
