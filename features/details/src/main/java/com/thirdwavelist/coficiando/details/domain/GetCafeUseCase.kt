package com.thirdwavelist.coficiando.details.domain

import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.coreutils.coroutines.CoroutineDispatcherProvider
import com.thirdwavelist.coficiando.coreutils.usecase.UseCase
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetCafeUseCase @Inject constructor(
        private val cafeRepository: CafeRepository,
        dispatcherProvider: CoroutineDispatcherProvider
) : UseCase<CafeItem?, UUID>(dispatcherProvider) {

    override suspend fun executeOnBackground(): CafeItem? = cafeRepository.getById(getParams())

}