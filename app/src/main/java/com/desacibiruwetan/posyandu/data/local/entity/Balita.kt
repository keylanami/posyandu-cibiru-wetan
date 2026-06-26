package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_balita")
data class BalitaEntity(
    @PrimaryKey(autoGenerate = true)
    val idLocalBalita: Int = 0,

    val anggotaLocalId: Int,
    val anggotaServerId: Int? = null,

    val tinggiBadan: Double,
    val beratBadan: Double,
    val isSynced: Boolean = false
)
