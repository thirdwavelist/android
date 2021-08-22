package com.thirdwavelist.coficiando.home.presentation

import android.view.View
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe

sealed class HomeEvents {
    data class NavigateToDetails(val cafeId: Cafe, val sharedElementTransitions: List<Pair<View, String>>) : HomeEvents()
    object ReloadData : HomeEvents()
}