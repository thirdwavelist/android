package com.thirdwavelist.coficiando.core.data.db.cafe

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.UUID

@Dao
interface CafeDao {
    @Query("select * from cafes")
    fun getAll(): Flowable<List<CafeItem>>

    @Query("select * from cafes where id = :id limit 1")
    fun get(id: UUID): Single<CafeItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cafes: List<CafeItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cafe: CafeItem)

    @Delete
    fun delete(cafeItem: CafeItem)
}