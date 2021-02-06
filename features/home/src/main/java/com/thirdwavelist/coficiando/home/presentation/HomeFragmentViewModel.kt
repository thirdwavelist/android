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
        private val getAllCafesUseCase: GetAllCafesUseCase
) : ViewModel() {

    private val _cafes: MutableLiveData<List<CafeItem>> = MutableLiveData()

    val cafes: LiveData<List<CafeItem>>
        get() = _cafes

    internal fun loadCafes() {
        getAllCafesUseCase.withParams(None).execute {
            onComplete {
                _cafes.value = it
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
