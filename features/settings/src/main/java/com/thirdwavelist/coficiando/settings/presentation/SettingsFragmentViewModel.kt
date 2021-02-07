package com.thirdwavelist.coficiando.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.di.qualifiers.FeedbackLink
import com.thirdwavelist.coficiando.core.di.qualifiers.PrivacyPolicyLink
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingsFragmentViewModel @Inject constructor(
        @PrivacyPolicyLink private val privacyPolicyLink: String,
        @FeedbackLink private val feedbackLink: String
) : ViewModel() {
    private val _onPrivacyPolicyTapped: MutableLiveData<String> = MutableLiveData()
    val onPrivacyPolicyTapped: LiveData<String> get() = _onPrivacyPolicyTapped

    private val _onFeedbackTapped: MutableLiveData<String> = MutableLiveData()
    val onFeedbackTapped: LiveData<String> get() = _onFeedbackTapped

    fun privacyPolicyTapped() {
        _onPrivacyPolicyTapped.postValue(privacyPolicyLink)
    }

    fun feedbackTapped() {
        _onFeedbackTapped.postValue(feedbackLink)
    }
}