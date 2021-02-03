package com.thirdwavelist.coficiando.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thirdwavelist.coficiando.core.data.db.cafe.CafeDao
import com.thirdwavelist.coficiando.core.data.db.cafe.CafeItem

@Database(version = 1, entities = [CafeItem::class])
@TypeConverters(CustomTypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun cafeDao(): CafeDao
}