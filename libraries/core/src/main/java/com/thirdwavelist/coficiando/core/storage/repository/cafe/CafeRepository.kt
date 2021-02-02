@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.thirdwavelist.coficiando.core.storage.repository.cafe

import com.thirdwavelist.coficiando.core.network.thirdwavelist.ThirdWaveListService
import com.thirdwavelist.coficiando.core.storage.Resource
import com.thirdwavelist.coficiando.core.storage.db.cafe.*
import com.thirdwavelist.coficiando.core.storage.repository.Repository
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

        return createCombinedFlowable(local, remote, { response ->
            response
                    .filter { it.isValid() }
                    .map {
                        CafeItem(id = it.id,
                                name = it.name!!,
                                thumbnail = it.thumbnail!!,
                                social = SocialItem(facebookUri = if (it.socialFacebook != null && it.socialFacebook.toString().isNotBlank()) it.socialFacebook else null,
                                        instagramUri = if (it.socialInstagram != null && it.socialInstagram.toString().isNotBlank()) it.socialInstagram else null,
                                        homepageUri = if (it.socialWebsite != null && it.socialWebsite.toString().isNotBlank()) it.socialWebsite else null),
                                googlePlaceId = it.googlePlaceId!!,
                                gearInfo = GearInfoItem(espressoMachineName = it.gearEspressoMachine,
                                        grinderMachineName = it.gearGrinder),
                                beanInfo = BeanInfoItem(origin = it.beanOrigin,
                                        roaster = it.beanRoaster,
                                        hasSingleOrigin = it.beanOriginSingle ?: false,
                                        hasBlendOrigin = it.beanOriginBlend ?: false,
                                        hasLightRoast = it.beanRoastLight ?: false,
                                        hasMediumRoast = it.beanRoastMedium ?: false,
                                        hasDarkRoast = it.beanRoastDark ?: false),
                                brewInfo = BrewInfoItem(hasEspresso = it.doesServeEspresso ?: false,
                                        hasAeropress = it.doesServeAeropress ?: false,
                                        hasColdBrew = it.doesServeColdBrew ?: false,
                                        hasFullImmersive = it.doesServeFullImmersive ?: false,
                                        hasPourOver = it.doesServePourOver ?: false,
                                        hasSyphon = it.doesServeSyphon ?: false)
                        )
                    }
                    .distinct()
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

    return Flowable.create({ emitter ->
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