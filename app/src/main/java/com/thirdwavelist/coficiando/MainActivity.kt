package com.thirdwavelist.coficiando

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.thirdwavelist.coficiando.design.CoficiandoTheme
import com.thirdwavelist.coficiando.navigation.Destination
import com.thirdwavelist.coficiando.presentation.CoficiandoApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val menuItems: Set<Destination> by lazy { hashSetOf(Destination.Home(), Destination.Settings()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            CoficiandoTheme {
                ProvideWindowInsets {
                    CoficiandoApp(onBackPressedDispatcher, menuItems)
                }
            }
        }
    }
}