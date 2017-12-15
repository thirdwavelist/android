package com.thirdwavelist.coficiando.storage.db.cafe

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import java.util.UUID

@Entity(tableName = "cafes")
data class CafeItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "thumb")
    val thumbnail: Uri,
    @Embedded
    val social: SocialItem,
    @ColumnInfo(name = "place_id")
    val googlePlaceId: String
)