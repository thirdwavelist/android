package com.thirdwavelist.coficiando.core.data.cafes

import com.haroldadmin.cnradapter.NetworkResponse
import com.thirdwavelist.coficiando.core.data.cafes.mapper.CafeItemDtoToCafeEntityMapper
import com.thirdwavelist.coficiando.core.data.db.CafeDao
import com.thirdwavelist.coficiando.core.data.network.ThirdWaveListService
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import com.thirdwavelist.coficiando.coreutils.Repository
import com.thirdwavelist.coficiando.coreutils.logging.BusinessEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorPriority
import java.util.UUID
import javax.inject.Inject

class CafeRepository @Inject constructor(
        private val dao: CafeDao,
        private val service: ThirdWaveListService,
        private val cafeMapper: CafeItemDtoToCafeEntityMapper,
        private val businessEventLogger: BusinessEventLogger,
        private val errorEventLogger: ErrorEventLogger
) : Repository<CafeItem> {

    override suspend fun getAll(): List<CafeItem> {
        val local: List<CafeItem> = dao.getAll()

        val remote: List<CafeItemDto> = when (val result = service.getCafes()) {
            is NetworkResponse.Success -> {
                businessEventLogger.log(BusinessEvent("CafeRepository_getAll_remote_success", mapOf(
                        "message" to "Successfully fetched latest cafes via #getAll()",
                        "cafeCount" to result.body.size
                )))
                result.body
            }
            is NetworkResponse.ServerError -> {
                errorEventLogger.log(
                        ErrorEvent(ErrorPriority.HIGH, TAG, Throwable(result.body?.message
                                ?: "Server error while calling #getAll()"))
                )
                emptyList()
            }
            is NetworkResponse.NetworkError -> {
                errorEventLogger.log(ErrorEvent(ErrorPriority.HIGH, TAG, result.error))
                emptyList()
            }
            is NetworkResponse.UnknownError -> {
                errorEventLogger.log(ErrorEvent(ErrorPriority.HIGH, TAG, result.error))
                emptyList()
            }
        }

        val mappedRemote = remote.filter { it.isValid() }.map(cafeMapper).distinct()
        if (mappedRemote.isNotEmpty() && mappedRemote != local) {
            dao.insertAll(mappedRemote)
            return mappedRemote
        }

        return local
    }

    override suspend fun get(uid: UUID): CafeItem? {
        val local: CafeItem? = dao.get(uid)

        val remote: CafeItemDto? = when (val result = service.getCafe(uid)) {
            is NetworkResponse.Success -> {
                businessEventLogger.log(
                        BusinessEvent("CafeRepository_get_remote_success", mapOf(
                                "message" to "Successfully fetched latest cafe with id: $uid",
                                "cafeId" to uid
                        )))
                result.body
            }
            is NetworkResponse.ServerError -> {
                errorEventLogger.log(
                        ErrorEvent(ErrorPriority.HIGH, TAG, Throwable(result.body?.message
                                ?: "Server error while calling #get(UUID)"))
                )
                null
            }
            is NetworkResponse.NetworkError -> {
                errorEventLogger.log(ErrorEvent(ErrorPriority.HIGH, TAG, result.error))
                null
            }
            is NetworkResponse.UnknownError -> {
                errorEventLogger.log(ErrorEvent(ErrorPriority.HIGH, TAG, result.error))
                null
            }
        }

        val mappedRemote: CafeItem? = if (remote != null && remote.isValid()) {
            cafeMapper(remote).also {
                if (it != local) {
                    dao.insert(it)
                }
            }
        } else null

        return mappedRemote ?: local
    }

    private companion object {
        private const val TAG = "CafeRepository"
    }
}
