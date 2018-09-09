package com.thirdwavelist.coficiando.storage.db.city

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri

@Entity(tableName = "cities")
data class CityItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "countryFlag")
    val countryFlag: String,
    @ColumnInfo(name = "label")
    val label: String
)

