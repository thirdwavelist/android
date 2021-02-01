package com.thirdwavelist.coficiando

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator
import com.thirdwavelist.coficiando.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), NavigationOrchestrator {
    private val navigator: Navigator by lazy { Navigator(findNavController(R.id.nav_host)) }

    override fun navigateToFlow(flow: NavigationFlow, sharedElementTransitions: Array<Pair<View, String>>) =
            navigator.navigateToFlow(flow, sharedElementTransitions)
}