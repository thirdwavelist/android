package com.thirdwavelist.coficiando.core

import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import java.util.UUID

interface Repository<T> {
    suspend fun get(uid: UUID): CafeItem?
    suspend fun getAll(): List<T>
}