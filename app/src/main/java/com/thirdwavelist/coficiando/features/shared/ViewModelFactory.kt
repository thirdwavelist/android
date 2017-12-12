package com.thirdwavelist.coficiando.features.shared

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.thirdwavelist.coficiando.features.home.HomeActivityViewModel
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem

class ViewModelFactory(private val repository: Repository<CafeItem>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)) {
            return HomeActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}