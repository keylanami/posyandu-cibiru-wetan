package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_wuspus", indices = [Index(value = ["server_id"], unique = true)])
data class WusPusEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    @ColumnInfo(name = "server_id")
    val serverId: Int?= null,

    val anggotaId: Int,
    val namaSuami: String?= null,
    val statusKategori: String,
    val tanggalMulaiStatus: String?= null,
    val keterangan: String?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false
)