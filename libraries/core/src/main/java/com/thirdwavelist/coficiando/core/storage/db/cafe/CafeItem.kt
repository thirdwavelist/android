package com.thirdwavelist.coficiando.core.storage.db.cafe

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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
        val googlePlaceId: String,
        @Embedded
        val gearInfo: GearInfoItem,
        @Embedded
        val beanInfo: BeanInfoItem,
        @Embedded
        val brewInfo: BrewInfoItem
)