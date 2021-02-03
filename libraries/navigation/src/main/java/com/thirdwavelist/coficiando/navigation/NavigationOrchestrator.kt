package com.thirdwavelist.coficiando.navigation

import android.view.View

interface NavigationOrchestrator {
    fun navigateToFlow(flow: NavigationFlow, sharedElementTransitions: Array<Pair<View, String>> = emptyArray())
}