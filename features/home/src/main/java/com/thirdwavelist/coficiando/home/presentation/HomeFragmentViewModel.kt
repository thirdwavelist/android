package com.thirdwavelist.coficiando.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.coreutils.usecase.None
import com.thirdwavelist.coficiando.home.domain.GetAllCafesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeFragmentViewModel @Inject constructor(
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
