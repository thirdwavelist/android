package com.thirdwavelist.coficiando.home.presentation

import com.thirdwavelist.coficiando.core.domain.cafe.Cafe

sealed class HomeViewState {
    object Loading : HomeViewState()
    object Error : HomeViewState()
    data class Success(val cafes: List<Cafe>) : HomeViewState()
}