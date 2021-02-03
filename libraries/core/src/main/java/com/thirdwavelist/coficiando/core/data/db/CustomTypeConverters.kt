package com.thirdwavelist.coficiando.core.data.db

import android.net.Uri
import androidx.room.TypeConverter
import java.util.UUID

class CustomTypeConverters {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)

    @TypeConverter
    fun fromUri(value: Uri?): String = value?.toString() ?: Uri.EMPTY.toString()

    @TypeConverter
    fun toUri(value: String): Uri = Uri.parse(value)
}