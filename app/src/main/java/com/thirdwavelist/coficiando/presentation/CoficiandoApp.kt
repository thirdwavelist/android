package com.thirdwavelist.coficiando.presentation

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.thirdwavelist.coficiando.navigation.Actions
import com.thirdwavelist.coficiando.navigation.Destination
import com.thirdwavelist.coficiando.navigation.Navigator
import com.thirdwavelist.coficiando.details.presentation.DetailsScreen
import com.thirdwavelist.coficiando.home.presentation.HomeScreen
import com.thirdwavelist.coficiando.settings.presentation.SettingsScreen

@Composable
internal fun CoficiandoApp(onBackPressedDispatcher: OnBackPressedDispatcher, menuItems: Set<Destination>) {
    val menuOptions = remember { menuItems }
    val navigator: Navigator<Destination> = rememberSaveable(
        saver = Navigator.saver(onBackPressedDispatcher)
    ) {
        Navigator(listOf(Destination.Home()), onBackPressedDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }
    Scaffold(
        bottomBar = {
            BottomNavigation {
                menuOptions.forEach {
                    when (it) {
                        is Destination.Home -> {
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Home, stringResource(it.titleRes)) },
                                label = { Text(stringResource(id = it.titleRes)) },
                                selected = navigator.current.route.equals(it.route, ignoreCase = true),
                                alwaysShowLabel = false,
                                onClick = {
                                    actions.navigateToHome.invoke()
                                }
                            )
                        }
                        is Destination.Settings -> {
                            BottomNavigationItem(
                                icon = { Icon(Icons.Default.Settings, stringResource(it.titleRes)) },
                                label = { Text(stringResource(it.titleRes)) },
                                selected = navigator.current.route.equals(it.route, ignoreCase = true),
                                alwaysShowLabel = false,
                                onClick = {
                                    actions.navigateToSettings.invoke()
                                }
                            )
                        }
                        else -> throw UnsupportedOperationException("Not supported in bottom navigation bar: ${it::class}")
                    }
                }
            }
        },
    ) {
        Crossfade(navigator.current) { destination ->
            when (destination) {
                is Destination.Home -> HomeScreen()
                is Destination.Details -> DetailsScreen(
                    id = destination.id,
                    upPress = actions.upPress
                )
                is Destination.Settings -> SettingsScreen(
                    upPress = actions.upPress
                )
            }
        }
    }
}