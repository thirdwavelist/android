package com.thirdwavelist.coficiando.features.home

import android.app.SearchManager
import android.arch.lifecycle.ViewModel
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.support.v7.widget.SearchView
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.Status
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


class HomeActivityViewModel(private val repository: Repository<CafeItem>,
                            val adapter: CafeAdapter) : ViewModel() {

    private val disposables = CompositeDisposable()

    val autocompleteAdapter = BehaviorSubject.create<Cursor>()

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
                } else {
                    loadCafes()
                }
                return true
            }
        })

        subject
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

                    updateAutocomplete(it.data)
                }
            }
            Status.ERROR -> {
            }
        }
    }

    private fun updateAutocomplete(data: List<CafeItem>) {
        Observable.fromCallable {
            MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)).also { cursor ->
                var i = 0;
                data.map { String(it.name.toCharArray()) }.distinct().forEach {
                    cursor.addRow(arrayOf(i++, it))
                }
            }
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ autocompleteAdapter.onNext(it) }, { /* do nothing */})
    }
}
