package com.thirdwavelist.coficiando.details.domain

import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe
import com.thirdwavelist.coficiando.core.util.coroutines.CoroutineDispatcherProvider
import com.thirdwavelist.coficiando.core.util.usecase.UseCase
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetCafeUseCase @Inject constructor(
    private val cafeRepository: CafeRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : UseCase<Cafe?, UUID>(dispatcherProvider) {

    override suspend fun executeOnBackground(): Cafe? = cafeRepository.getById(getParams())

}