package com.thirdwavelist.coficiando.home.presentation

import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.None
import com.thirdwavelist.coficiando.home.domain.GetAllCafesUseCase

class HomeFragmentViewModel(
        private val getAllCafesUseCase: GetAllCafesUseCase,
        val adapter: CafeAdapter
) : ViewModel() {

    fun loadCafes() {
        getAllCafesUseCase.withParams(None).execute {
            onComplete {
                adapter.submitList(it)
            }
            onError {
                handleError()
            }
        }
    }

    private fun handleError() {
        TODO("Not yet implemented")
    }
}
