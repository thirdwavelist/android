package com.thirdwavelist.coficiando.features.shared

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.widget.RecyclerView
import com.thirdwavelist.coficiando.features.home.CafeAdapter
import com.thirdwavelist.coficiando.features.home.HomeActivityViewModel
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem

class ViewModelFactory(private val repository: Repository<CafeItem>, private val adapter: CafeAdapter) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            HomeActivityViewModel::class -> return HomeActivityViewModel(repository, adapter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}