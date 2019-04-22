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

package com.thirdwavelist.coficiando.storage.repository

import com.thirdwavelist.coficiando.storage.Resource
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface Repository<T> {
    fun get(tId: String): Single<T>
    fun getAll(): Flowable<Resource<List<T>>>
    fun insert(t: T)
    fun insertAll(ts: List<T>)
    fun delete(t: T)
}

fun <LocalType, RemoteType> createCombinedFlowable(local: Flowable<LocalType>,
                                                   remote: Single<RemoteType>,
                                                   mapper: io.reactivex.functions.Function<RemoteType, LocalType>,
                                                   persist: (LocalType) -> Unit = {}): Flowable<Resource<LocalType>> {

    return Flowable.create<Resource<LocalType>>({ emitter ->
        emitter.setDisposable(local
            .map { Resource.loading(it) }
            .subscribe { emitter.onNext(it) })

        remote.map(mapper)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe({ localTypeData ->
                persist(localTypeData)
                emitter.setDisposable(local
                    .map { Resource.success(it) }
                    .subscribe { emitter.onNext(it) })
            }, { error ->
                emitter.onNext(Resource.error(error))
            })
    }, BackpressureStrategy.LATEST)
}