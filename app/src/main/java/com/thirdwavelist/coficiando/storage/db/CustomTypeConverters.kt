package com.thirdwavelist.coficiando.storage.db

import android.arch.persistence.room.TypeConverter
import android.net.Uri
import java.util.UUID

class CustomTypeConverters {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)

    @TypeConverter
    fun fromUri(value: Uri?): String = if (value == null) Uri.EMPTY.toString() else value.toString()

    @TypeConverter
    fun toUri(value: String): Uri = Uri.parse(value)
}