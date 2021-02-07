package com.thirdwavelist.coficiando.home.presentation

import android.view.View
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem

sealed class HomeEvents {
    data class NavigateToDetails(val cafeId: CafeItem, val sharedElementTransitions: List<Pair<View, String>>) : HomeEvents()
    object ReloadData : HomeEvents()
}