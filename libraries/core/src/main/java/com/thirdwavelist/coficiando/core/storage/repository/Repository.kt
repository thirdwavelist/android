package com.thirdwavelist.coficiando.core.storage.repository

import com.thirdwavelist.coficiando.core.storage.Resource
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.UUID

interface Repository<T> {
    fun get(tId: UUID): Single<T>
    fun getAll(): Flowable<Resource<List<T>>>
    fun insert(t: T)
    fun insertAll(ts: List<T>)
    fun delete(t: T)
}