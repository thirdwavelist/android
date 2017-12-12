package com.thirdwavelist.coficiando.storage.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.thirdwavelist.coficiando.storage.db.cafe.CafeDao
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem

@Database(version = 1, entities = [CafeItem::class])
@TypeConverters(CustomTypeConverters::class)
abstract class Database: RoomDatabase() {
    abstract fun cafeDao(): CafeDao
}