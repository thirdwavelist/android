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

class HomeActivityViewModel(private val repository: Repository<CafeItem>,
                            val adapter: CafeAdapter) : ViewModel() {

    private val disposables = CompositeDisposable()

    private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun getCafes() {
        disposables += repository
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

    fun dispose() {
        disposables.clear()
    }

    private fun handleError() {
        TODO("not implemented yet")
    }

    private fun handleResponse(it: Resource<List<CafeItem>>) {
        when (it.status) {
            Status.LOADING -> {
                if (it.data != null && it.data.isNotEmpty()) {
                    adapter.data = it.data
                }
            }
            Status.SUCCESS -> {
                if (it.data != null && it.data.isNotEmpty()) {
                    adapter.data = it.data
                }
            }
            Status.ERROR -> {
            }
        }
    }
}
