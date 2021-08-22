package com.thirdwavelist.coficiando.details.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.livedata.LiveEvent
import com.thirdwavelist.coficiando.details.domain.GetCafeUseCase
import com.thirdwavelist.coficiando.details.presentation.mapper.MapEntityToDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCafeUseCase: GetCafeUseCase,
    private val viewStateMapper: MapEntityToDetailsViewState
) : ViewModel() {
    private var _viewState: MutableLiveData<DetailsViewState> = MutableLiveData(DetailsViewState.Loading)
    val viewState: LiveData<DetailsViewState> get() = _viewState

    private var _viewEvents: MutableLiveData<LiveEvent<DetailsEvents>> = MutableLiveData()
    val viewEvents: LiveData<LiveEvent<DetailsEvents>> get() = _viewEvents

    fun loadCafe(cafeId: UUID) {
        getCafeUseCase.withParams(cafeId).execute {
            onLoadingChange { isItLoading ->
                if (isItLoading) _viewState.postValue(DetailsViewState.Loading)
            }
            onComplete {
                if (it != null) {
                    _viewState.postValue(viewStateMapper(it))
                } else {
                    _viewState.postValue(DetailsViewState.Error)
                }
            }
            onError {
                _viewState.postValue(DetailsViewState.Error)
            }
            onCancel {
                _viewState.postValue(DetailsViewState.Error)
            }
        }
    }

    @MainThread
    internal fun onLinkButtonTapped(withUrl: String) {
        _viewEvents.value = LiveEvent(DetailsEvents.NavigateToUrl(withUrl))
    }

    @MainThread
    internal fun onRetryTapped() {
        _viewEvents.value = LiveEvent(DetailsEvents.ReloadData)
    }
}