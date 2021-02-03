package com.thirdwavelist.coficiando

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LottieAnimationView>(R.id.animation_view).addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit

            override fun onAnimationEnd(animation: Animator?) {
                navigateToHome()
            }

            override fun onAnimationCancel(animation: Animator?) {
                navigateToHome()
            }

            override fun onAnimationRepeat(animation: Animator?) = Unit
        })
    }

    private fun navigateToHome() = (requireActivity() as NavigationOrchestrator).navigateToFlow(NavigationFlow.HomeFlow)
}