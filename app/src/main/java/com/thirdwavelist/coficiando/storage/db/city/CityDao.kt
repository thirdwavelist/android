package com.thirdwavelist.coficiando.storage.db.city

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CityDao {
    @Query("select * from cities")
    fun getAll(): Flowable<List<CityItem>>

    @Query("select * from cities where id = :id limit 1")
    fun get(id: String): Single<CityItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cafes: List<CityItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cafe: CityItem)

    @Delete
    fun delete(cafeItem: CityItem)
}