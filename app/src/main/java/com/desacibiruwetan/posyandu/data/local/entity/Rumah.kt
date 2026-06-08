package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_rumah")
data class RumahEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    val serverId: Int?= null,

    val rtId: Int?= null,
    val noRumah: Int?= null,
    val alamat: String?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,

    val isSynced: Boolean = false

)
