/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Antal János Monori
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thirdwavelist.coficiando.features.home

import androidx.lifecycle.ViewModel
import androidx.appcompat.widget.SearchView
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.Status
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.repository.Repository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class HomeActivityViewModel(
    private val cafeRepository: Repository<CafeItem>,
    val cafeAdapter: CafeAdapter
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun loadCafes() {
        disposables += cafeRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .safeSubscribe {
                handleCafeResponse(it)
            }
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
            .filter { item -> item.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                cafeAdapter.filter.filter(query)
            }
    }

    fun dispose() {
        disposables.clear()
    }

    private fun handleError() {
        TODO("not implemented yet")
    }

    private fun handleCafeResponse(it: Resource<List<CafeItem>>) {
        when (it.status) {
            Status.LOADING -> {
                if (it.data != null && it.data.isNotEmpty()) {
                    cafeAdapter.data = it.data
                }
            }
            Status.SUCCESS -> {
                if (it.data != null && it.data.isNotEmpty()) {
                    cafeAdapter.data = it.data
                    cafeAdapter.initialData = it.data.toMutableList()
                }
            }
            Status.ERROR -> {
                // do nothing
            }
        }
    }

    private fun <T> Flowable<T>.safeSubscribe(lambda: (T) -> Unit) =
        this.subscribe(
            {
                /* onNext */
                lambda.invoke(it)
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
