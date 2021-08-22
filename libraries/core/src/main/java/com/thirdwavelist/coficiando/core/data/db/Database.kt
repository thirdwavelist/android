package com.thirdwavelist.coficiando.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thirdwavelist.coficiando.core.domain.cafe.Cafe

@Database(version = 1, entities = [Cafe::class])
@TypeConverters(CustomTypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
}