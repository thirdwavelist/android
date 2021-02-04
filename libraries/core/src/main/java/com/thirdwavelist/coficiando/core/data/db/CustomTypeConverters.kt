package com.thirdwavelist.coficiando.core.data.db

import androidx.room.TypeConverter
import java.util.UUID

class CustomTypeConverters {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}