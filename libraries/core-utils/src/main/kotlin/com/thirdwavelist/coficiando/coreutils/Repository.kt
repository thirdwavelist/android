package com.thirdwavelist.coficiando.coreutils

import java.util.UUID

interface Repository<T> {
    suspend fun get(uid: UUID): T?
    suspend fun getAll(): List<T>
}