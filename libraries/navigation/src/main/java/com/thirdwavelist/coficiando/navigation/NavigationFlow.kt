package com.thirdwavelist.coficiando.navigation

sealed class NavigationFlow {
    object HomeFlow : NavigationFlow()
    data class DetailsFlow(val id: String) : NavigationFlow()
    object SettingsFlow : NavigationFlow()
}