package com.thirdwavelist.coficiando.home.domain

import com.thirdwavelist.coficiando.core.None
import com.thirdwavelist.coficiando.core.UseCase
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import javax.inject.Inject

class GetAllCafesUseCase @Inject constructor(private val cafeRepository: CafeRepository)
    : UseCase<List<CafeItem>, None>() {

    override suspend fun executeOnBackground(): List<CafeItem> {
        return cafeRepository.getAll()
    }

}