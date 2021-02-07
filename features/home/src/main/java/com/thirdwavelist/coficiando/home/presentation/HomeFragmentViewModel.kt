package com.thirdwavelist.coficiando.home.presentation

import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.core.livedata.LiveEvent
import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import com.thirdwavelist.coficiando.coreutils.logging.BusinessEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorPriority
import com.thirdwavelist.coficiando.coreutils.usecase.None
import com.thirdwavelist.coficiando.home.domain.GetAllCafesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeFragmentViewModel @Inject constructor(
        private val getAllCafesUseCase: GetAllCafesUseCase,
        private val errorEventLogger: ErrorEventLogger,
        private val businessEventLogger: BusinessEventLogger
) : ViewModel() {

    private val _viewState: MutableLiveData<HomeViewState> = MutableLiveData(HomeViewState.Loading)
    val viewState: LiveData<HomeViewState> get() = _viewState

    private val _viewEvents: MutableLiveData<LiveEvent<HomeEvents>> = MutableLiveData()
    val viewEvents: LiveData<LiveEvent<HomeEvents>> get() = _viewEvents

    internal fun loadCafes() {
        getAllCafesUseCase.withParams(None).execute {
            onLoadingChange { isItLoading ->
                if (isItLoading) _viewState.postValue(HomeViewState.Loading)
            }
            onComplete {
                if (it.isNotEmpty()) {
                    _viewState.value = HomeViewState.Success(it)
                    businessEventLogger.log(
                            BusinessEvent("${TAG}_successful_loadCafes", mapOf(
                                    "cafeCount" to it.size
                            )))
                } else {
                    _viewState.postValue(HomeViewState.Error)
                    errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, Throwable("No items was received")))
                }
            }
            onError {
                _viewState.postValue(HomeViewState.Error)
                errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, it))
            }
            onCancel {
                _viewState.postValue(HomeViewState.Error)
                errorEventLogger.log(ErrorEvent(ErrorPriority.CRITICAL, TAG, it))
            }
        }
    }

    @MainThread
    internal fun onCafeTapped(cafeItem: CafeItem, sharedElementTransitions: List<Pair<View, String>>) {
        _viewEvents.value = LiveEvent(HomeEvents.NavigateToDetails(cafeItem, sharedElementTransitions))
    }

    @MainThread
    internal fun onRetryTapped() {
        _viewEvents.value = LiveEvent(HomeEvents.ReloadData)
    }

    private companion object {
        private const val TAG = "HomeFragmentViewModel"
    }
}
