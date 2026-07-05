package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_rumah", indices = [Index(value = ["server_id"], unique = true)] )
data class RumahEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    @ColumnInfo(name = "server_id")
    val serverId: Int?= null,

    val rtId: Int?= null,
    val noRumah: String?= null,
    val dusun: String?= null,
    val alamat: String?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,

    val isSynced: Boolean = false

)
