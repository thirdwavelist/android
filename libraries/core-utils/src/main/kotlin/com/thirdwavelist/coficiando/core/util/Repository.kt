package com.thirdwavelist.coficiando.core.util

import java.util.UUID

interface Repository<T> {
    suspend fun getById(uid: UUID): T?
    suspend fun getAll(): List<T>
}