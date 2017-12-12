package com.thirdwavelist.coficiando.storage.db.cafe

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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