package com.thirdwavelist.coficiando.features.home

import android.arch.lifecycle.ViewModel
import android.support.v7.widget.SearchView
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.Status
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


class HomeActivityViewModel(private val repository: Repository<CafeItem>,
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

    fun enableSearch(searchView: SearchView) {
        val subject = BehaviorSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!newText.isEmpty()) {
                    subject.onNext(newText)
                }
                return true
            }
        })

        disposables += subject
            .subscribeOn(Schedulers.computation())
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter({ item -> item.length > 1 })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ query ->
                adapter.filter.filter(query)
            })
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
                    adapter.initialData = it.data.toMutableList()
                }
            }
            Status.ERROR -> {
            }
        }
    }
}
