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

@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.thirdwavelist.coficiando.storage.repository.cafe

import com.thirdwavelist.coficiando.network.thirdwavelist.ThirdWaveListService
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.db.cafe.BeanInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.BrewInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.CafeDao
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.db.cafe.GearInfoItem
import com.thirdwavelist.coficiando.storage.db.cafe.SocialItem
import com.thirdwavelist.coficiando.storage.repository.Repository
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