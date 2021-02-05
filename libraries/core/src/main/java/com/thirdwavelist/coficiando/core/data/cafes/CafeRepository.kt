package com.thirdwavelist.coficiando.core.data.cafes

import com.haroldadmin.cnradapter.NetworkResponse
import com.thirdwavelist.coficiando.core.Repository
import com.thirdwavelist.coficiando.core.data.cafes.mapper.CafeItemDtoToCafeEntityMapper
import com.thirdwavelist.coficiando.core.data.db.CafeDao
import com.thirdwavelist.coficiando.core.data.network.ThirdWaveListService
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class CafeRepository @Inject constructor(
        private val dao: CafeDao,
        private val service: ThirdWaveListService,
        private val cafeMapper: CafeItemDtoToCafeEntityMapper
) : Repository<CafeItem> {

    override suspend fun getAll(): List<CafeItem> {
        val local: List<CafeItem> = dao.getAll()

        val remote: List<CafeItemDto> = when (val result = service.getCafes()) {
            is NetworkResponse.Success -> {
                Timber.tag("CafeRepository").d("Successfully fetched latest cafes")
                result.body
            }
            is NetworkResponse.ServerError -> {
                handleNetworkError(Throwable(result.body?.message ?: "Server error"))
                emptyList()
            }
            is NetworkResponse.NetworkError -> {
                handleNetworkError(result.error)
                emptyList()
            }
            is NetworkResponse.UnknownError -> {
                handleNetworkError(result.error)
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
                Timber.tag("CafeRepository").d("Successfully fetched latest cafe with id: $uid")
                result.body
            }
            is NetworkResponse.ServerError -> {
                handleNetworkError(Throwable(result.body?.message ?: "Server error"))
                null
            }
            is NetworkResponse.NetworkError -> {
                handleNetworkError(result.error)
                null
            }
            is NetworkResponse.UnknownError -> {
                handleNetworkError(result.error)
                null
            }
        }

        val mappedRemote: CafeItem? = if (remote != null && remote.isValid() && remote.equals(local).not()) {
            cafeMapper(remote).also { dao.insert(it) }
        } else null

        return mappedRemote ?: local
    }

    private fun handleNetworkError(throwable: Throwable) {
        Timber.tag("CafeRepository").e(throwable)
    }
}
