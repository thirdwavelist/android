package com.thirdwavelist.coficiando.home.domain

import com.thirdwavelist.coficiando.core.util.coroutines.CoroutineDispatcherProvider
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe
import com.thirdwavelist.coficiando.core.util.usecase.None
import com.thirdwavelist.coficiando.core.util.usecase.UseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAllCafesUseCase @Inject constructor(
    private val cafeRepository: CafeRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : UseCase<List<Cafe>, None>(dispatcherProvider) {

    override suspend fun executeOnBackground(): List<Cafe> {
        return cafeRepository.getAll()
    }

}