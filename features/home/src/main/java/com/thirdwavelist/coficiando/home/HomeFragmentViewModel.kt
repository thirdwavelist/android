package com.thirdwavelist.coficiando.home

import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.storage.Resource
import com.thirdwavelist.coficiando.core.storage.Status
import com.thirdwavelist.coficiando.core.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.core.storage.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeFragmentViewModel(private val repository: Repository<CafeItem>,
                            val adapter: CafeAdapter) : ViewModel() {

    private val disposables = CompositeDisposable()

    private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun loadCafes() {
        disposables += repository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    /* onNext */
                    handleResponse(it)
                },
                {
                    /* onError */
                    handleError()
                },
                {
                    /* onComplete */
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
                val data = it.data
                if (data != null && data.isNotEmpty()) {
                    adapter.submitList(data)
                }
            }
            Status.SUCCESS -> {
                val data = it.data
                if (data != null && data.isNotEmpty()) {
                    adapter.submitList(data)
                }
            }
            Status.ERROR -> {
            }
        }
    }
}
