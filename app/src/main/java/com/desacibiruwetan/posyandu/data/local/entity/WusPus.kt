package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_wuspus")
data class WusPusEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocalWusPus: Int = 0,

    val wusPusServerId: Int?= null,
    val anggotaLocalId: Int,
    val anggotaServerId: Int?= null,

    val namaSuami: String?= null,
    val statusKategori: String,
    val tanggalMulaiStatus: String?= null,
    val keterangan: String?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false
)