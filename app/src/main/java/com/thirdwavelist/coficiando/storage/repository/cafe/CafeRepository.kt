@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.thirdwavelist.coficiando.storage.repository.cafe

import com.thirdwavelist.coficiando.network.thirdwavelist.ThirdWaveListService
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.db.cafe.CafeDao
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.UUID
import javax.inject.Inject

class CafeRepository @Inject constructor(private val dao: CafeDao,
                                         private val service: ThirdWaveListService) : Repository<CafeItem> {

    override fun getAll(): Flowable<Resource<List<CafeItem>>> {
        val local = dao.getAll()
        val remote = service.getCafes()

        return createCombinedFlowable(local, remote, Function { response ->
            response
                .filter { it.isValid() }
                .map { CafeItem(it.id, it.name!!, it.thumbnail!!) }
        }, { dao.insertAll(it) })
    }

    override fun get(cafeId: UUID): Single<CafeItem> = dao.get(cafeId)

    override fun insert(cafe: CafeItem) = dao.insert(cafe)

    override fun insertAll(cafes: List<CafeItem>) = dao.insertAll(cafes)

    override fun delete(cafe: CafeItem) = dao.delete(cafe)
}

fun <LocalType, RemoteType> createCombinedFlowable(local: Flowable<LocalType>,
                                                   remote: Single<RemoteType>,
                                                   mapper: Function<RemoteType, LocalType>,
                                                   persist: (LocalType) -> Unit = {}): Flowable<Resource<LocalType>> {

    return Flowable.create<Resource<LocalType>>({ emitter ->
        emitter.setDisposable(local
            .map({ Resource.loading(it) })
            .subscribe({ emitter.onNext(it) }))

        remote.map(mapper)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe({ localTypeData ->
                persist(localTypeData)
                emitter.setDisposable(local
                    .map({ Resource.success(it) })
                    .subscribe({ emitter.onNext(it) }))
            }, { error ->
                emitter.onNext(Resource.error(error))
            })
    }, BackpressureStrategy.LATEST)
}