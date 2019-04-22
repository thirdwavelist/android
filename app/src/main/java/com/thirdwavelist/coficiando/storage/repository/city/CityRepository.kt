package com.thirdwavelist.coficiando.storage.repository.city

import com.thirdwavelist.coficiando.network.city.CityApi
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.db.city.CityDao
import com.thirdwavelist.coficiando.storage.db.city.CityItem
import com.thirdwavelist.coficiando.storage.db.common.LocationItem
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.repository.createCombinedFlowable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import javax.inject.Inject

class CityRepository @Inject constructor(private val dao: CityDao, private val service: CityApi) : Repository<CityItem> {

    override fun getAll(): Flowable<Resource<List<CityItem>>> {
        val local = dao.getAll().toFlowable()
        val remote = service.getCities()

        return createCombinedFlowable(local, remote, Function { response ->
            response
                .map {
                    CityItem(
                        id = it.id,
                        cityName = it.cityName,
                        countryCode = it.countryCode,
                        location = LocationItem(it.location.latitude, it.location.longitude)
                    )
                }
                .distinct()
        }) { dao.insertAll(it) }
    }

    override fun get(tId: String): Single<CityItem> = dao.get(tId)

    override fun insert(t: CityItem) = dao.insert(t)

    override fun insertAll(ts: List<CityItem>) = dao.insertAll(ts)

    override fun delete(t: CityItem) = dao.delete(t)
}