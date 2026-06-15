package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tabel_bumil")
data class BumilEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocalBumil: Int = 0,
    val anggotaLocalId: Int,
    val anggotaServerId: Int? = null,
    val bumilServerId: Int? = null,
    val asiEksklusif: Boolean,
    val hamilKe: Int,
    val tanggalMulaiAsi: String?= null,
    val tanggalSelesaiAsi: String?= null,
    val isSynced: Boolean = false,
    val createdAt: String?,
    val updatedAt: String?
)
