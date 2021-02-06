package com.thirdwavelist.coficiando.details.domain

import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.coreutils.coroutines.CoroutineDispatcherProvider
import com.thirdwavelist.coficiando.coreutils.usecase.UseCase
import java.util.UUID
import javax.inject.Inject


class GetCafeUseCase @Inject constructor(
        private val cafeRepository: CafeRepository,
        dispatcherProvider: CoroutineDispatcherProvider
) : UseCase<CafeItem?, UUID>(dispatcherProvider) {

    override suspend fun executeOnBackground(): CafeItem? = cafeRepository.get(getParams())

}