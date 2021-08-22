package com.thirdwavelist.coficiando.home.presentation

import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe
import com.thirdwavelist.coficiando.core.livedata.LiveEvent
import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import com.thirdwavelist.coficiando.core.util.logging.BusinessEvent
import com.thirdwavelist.coficiando.core.util.logging.ErrorEvent
import com.thirdwavelist.coficiando.core.util.logging.ErrorPriority
import com.thirdwavelist.coficiando.core.util.usecase.None
import com.thirdwavelist.coficiando.home.domain.GetAllCafesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCafesUseCase: GetAllCafesUseCase,
    private val errorEventLogger: ErrorEventLogger,
    private val businessEventLogger: BusinessEventLogger
) : ViewModel() {

    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState> get() = _viewState

    private val _viewEvents: MutableLiveData<LiveEvent<HomeEvents>> = MutableLiveData()
    val viewEvents: LiveData<LiveEvent<HomeEvents>> get() = _viewEvents

    init {
        loadCafes()
    }

    internal fun loadCafes() {
        getAllCafesUseCase.withParams(None).execute {
            onLoadingChange { isItLoading ->
                if (isItLoading) {
                    viewModelScope.launch {
                        _viewState.emit(HomeViewState.Loading)
                    }
                }
            }
            onComplete {
                if (it.isNotEmpty()) {
                    _viewState.value = HomeViewState.Success(it)
                    businessEventLogger.log(
                        BusinessEvent("${TAG}_successful_loadCafes", mapOf(
                            "cafeCount" to it.size
                        )))
                } else {
                    viewModelScope.launch {
                        _viewState.emit(HomeViewState.Error)
                    }
                    errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, Throwable("No items was received")))
                }
            }
            onError {
                viewModelScope.launch {
                    _viewState.emit(HomeViewState.Error)
                }
                errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, it))
            }
            onCancel {
                viewModelScope.launch {
                    _viewState.emit(HomeViewState.Error)
                }
                errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, it))
            }
        }
    }

    @MainThread
    internal fun onCafeTapped(cafe: Cafe, sharedElementTransitions: List<Pair<View, String>>) {
        _viewEvents.value = LiveEvent(HomeEvents.NavigateToDetails(cafe, sharedElementTransitions))
    }

    @MainThread
    internal fun onRetryTapped() {
        _viewEvents.value = LiveEvent(HomeEvents.ReloadData)
    }

    private companion object {
        private const val TAG = "HomeFragmentViewModel"
    }
}
