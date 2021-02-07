package com.thirdwavelist.coficiando.home.presentation

import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem

sealed class HomeViewState {
    object Loading : HomeViewState()
    object Error : HomeViewState()
    data class Success(
            val cafes: List<CafeItem>
    ) : HomeViewState()
}