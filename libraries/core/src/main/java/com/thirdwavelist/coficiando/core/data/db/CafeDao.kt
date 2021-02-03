package com.thirdwavelist.coficiando.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import java.util.UUID

@Dao
interface CafeDao {
    @Query("select * from cafes")
    suspend fun getAll(): List<CafeItem>

    @Query("select * from cafes where id = :id limit 1")
    suspend fun get(id: UUID): CafeItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cafes: List<CafeItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cafe: CafeItem)
}