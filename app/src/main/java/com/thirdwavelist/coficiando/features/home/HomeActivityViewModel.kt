package com.thirdwavelist.coficiando.features.home

import android.arch.lifecycle.ViewModel
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeActivityViewModel(private val cafeRepository: Repository<CafeItem>) : ViewModel() {

    private val disposables = CompositeDisposable()

    private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun getCafes() {
        disposables += cafeRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {/* onNext */
                    handleResponse(it)
                },
                {/* onError */
                    handleError()
                },
                {/* onComplete */
                    /* do nothing */
                }
            )
    }

    private fun handleError() {
        TODO("not implemented yet")
    }

    private fun handleResponse(it: Resource<List<CafeItem>>) {
        when (it.status) {
            Status.LOADING -> {
                if (it.data != null && it.data.isNotEmpty()) {
//                    adapter.replaceItems(it.data)
//                    isLoading = false
                }
            }
            Status.ERROR -> {
//                if (adapter.isEmpty()) {
//                    isError = true
//                    isLoading = false
//                }
            }
            Status.SUCCESS -> {
                if (it.data != null && it.data.isNotEmpty()) {
//                    adapter.replaceItems(it.data)
                }
//                isError = (adapter.isEmpty() && it.data?.isEmpty() ?: true)
//                isLoading = false
            }
        }

    }
}