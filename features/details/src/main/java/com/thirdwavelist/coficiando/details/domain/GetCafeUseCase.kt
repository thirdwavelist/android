package com.thirdwavelist.coficiando.details.domain

import com.thirdwavelist.coficiando.core.UseCase
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import java.util.UUID
import javax.inject.Inject

class GetCafeUseCase @Inject constructor(private val cafeRepository: CafeRepository)
    : UseCase<CafeItem?, UUID>() {

    override suspend fun executeOnBackground(): CafeItem? = cafeRepository.get(getParams())

}