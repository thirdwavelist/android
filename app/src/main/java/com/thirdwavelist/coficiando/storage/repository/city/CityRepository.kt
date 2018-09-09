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

package com.thirdwavelist.coficiando.storage.repository.city

import com.thirdwavelist.coficiando.network.thirdwavelist.ThirdWaveListService
import com.thirdwavelist.coficiando.storage.Resource
import com.thirdwavelist.coficiando.storage.db.city.CityDao
import com.thirdwavelist.coficiando.storage.db.city.CityItem
import com.thirdwavelist.coficiando.storage.repository.Repository
import com.thirdwavelist.coficiando.storage.repository.cafe.createCombinedFlowable
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CityRepository @Inject constructor(private val dao: CityDao,
                                         private val service: ThirdWaveListService
) : Repository<CityItem> {
    override fun get(tId: UUID): Single<CityItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(t: CityItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertAll(ts: List<CityItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: CityItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): Flowable<Resource<List<CityItem>>> {
        val local = dao.getAll()
        val remote = service.getCities()

        return createCombinedFlowable(local, remote, Function { response ->
            response
                .filter { it.isValid() }
                .map {
                    CityItem(id = it.id,
                        label = it.label!!,
                        countryFlag = it.countryFlag!!
                    )
                }
                .distinct()
        }) { dao.insertAll(it) }
    }

}
