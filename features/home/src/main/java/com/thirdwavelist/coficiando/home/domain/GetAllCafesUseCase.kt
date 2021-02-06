package com.thirdwavelist.coficiando.home.domain

import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.coreutils.coroutines.CoroutineDispatcherProvider
import com.thirdwavelist.coficiando.coreutils.usecase.None
import com.thirdwavelist.coficiando.coreutils.usecase.UseCase
import javax.inject.Inject

internal class GetAllCafesUseCase @Inject constructor(
        private val cafeRepository: CafeRepository,
        dispatcherProvider: CoroutineDispatcherProvider
) : UseCase<List<CafeItem>, None>(dispatcherProvider) {

    override suspend fun executeOnBackground(): List<CafeItem> {
        return cafeRepository.getAll()
    }

}