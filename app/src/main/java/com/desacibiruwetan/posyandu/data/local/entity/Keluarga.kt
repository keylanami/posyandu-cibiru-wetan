package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_keluarga", indices = [Index(value = ["serverId"], unique = true)])
data class KeluargaEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val serverId: Int?= null,
    val rumahId: Int,

    val noKK: String,
    val isNgontrak: Boolean,
    val isGakin: Boolean?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false
)
