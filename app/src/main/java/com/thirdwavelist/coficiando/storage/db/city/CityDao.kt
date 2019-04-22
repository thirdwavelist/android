package com.thirdwavelist.coficiando.storage.db.city

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface CityDao {
    @Query("select * from cities")
    fun getAll(): Single<List<CityItem>>

    @Query("select * from cities where id = :id limit 1")
    fun get(id: String): Single<CityItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cafes: List<CityItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cafe: CityItem)

    @Delete
    fun delete(cafeItem: CityItem)
}
