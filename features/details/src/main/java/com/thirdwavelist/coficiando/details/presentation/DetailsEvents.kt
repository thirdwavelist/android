package com.thirdwavelist.coficiando.details.presentation

sealed class DetailsEvents {
    data class NavigateToUrl(val url: String) : DetailsEvents()
    object ReloadData : DetailsEvents()
}