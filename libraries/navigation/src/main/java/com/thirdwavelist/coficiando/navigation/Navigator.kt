package com.thirdwavelist.coficiando.navigation

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.thirdwavelist.coficiando.coreutils.ext.exhaustive

class Navigator(private val navController: NavController) {

    fun navigateToFlow(navigationFlow: NavigationFlow, sharedElementTransitions: Array<Pair<View, String>>) {
        val extras = FragmentNavigatorExtras(*sharedElementTransitions)
        when (navigationFlow) {
            is NavigationFlow.HomeFlow -> navController.navigate(NavMainDirections.actionGlobalHomeFlow(), extras)
            is NavigationFlow.DetailsFlow -> navController.navigate(NavMainDirections.actionGlobalDetailsFlow(navigationFlow.id), extras)
        }.exhaustive
    }

}
