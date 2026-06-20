package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_phbs")
data class PhbsEntity(
    @PrimaryKey(true)
    val idPhbsLocal: Int = 0,

    val idPhbsServer: Int? = null,
    val patuhProtokolKesehatan: Int? = null,
    val rumahJambanSehat: Int? = null,
    val rumahAirBersih: Int? = null,
    val kasusDiare: Int? = null,
    val keluargaSadarGizi: Int? = null,
    val rumahTanpaAsapRokok: Int? = null,
    val babs: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isSynced: Boolean = false
)
