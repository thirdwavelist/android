package com.thirdwavelist.coficiando.storage.db.city

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thirdwavelist.coficiando.storage.db.common.LocationItem

@Entity(tableName = "cities")
data class CityItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "country_code")
    val countryCode: String,
    @Embedded(prefix = "city_")
    val location: LocationItem
)