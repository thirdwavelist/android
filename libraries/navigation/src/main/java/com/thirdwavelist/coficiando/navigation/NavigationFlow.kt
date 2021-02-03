package com.thirdwavelist.coficiando.navigation

sealed class NavigationFlow {
    object HomeFlow : NavigationFlow()
    data class DetailsFlow(val id: String) : NavigationFlow()
}

sealed class NavigationExtras {
    data class TransitionElement(val view: Any, val transitionName: String)
}