package com.thirdwavelist.coficiando.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.thirdwavelist.coficiando.MainActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.navigation.NavigationFlow
import com.thirdwavelist.coficiando.navigation.NavigationOrchestrator
import com.thirdwavelist.coficiando.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationOrchestrator {

    private lateinit var binding: MainActivityBinding

    private val fragmentNavigatorExtrasMapper: (Array<Pair<View, String>>) -> FragmentNavigator.Extras = {
        FragmentNavigatorExtras(*it)
    }
    private val navController: NavController by lazy { findNavController(R.id.nav_host) }
    private val navigator: Navigator by lazy { Navigator(navController, fragmentNavigatorExtrasMapper) }

    override fun navigateToFlow(flow: NavigationFlow, sharedElementTransitions: Array<Pair<View, String>>) {
        binding.bottomNavigation.isVisible = true
        navigator.navigateToFlow(flow, sharedElementTransitions)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.home -> {
                    navigateToFlow(NavigationFlow.HomeFlow)
                    true
                }
                R.id.settings -> {
                    navigateToFlow(NavigationFlow.SettingsFlow)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(null)
        super.onDestroy()
    }
}