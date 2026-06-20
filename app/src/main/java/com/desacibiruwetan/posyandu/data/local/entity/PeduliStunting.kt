package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_peduli_stunting")
data class PeduliStuntingEntity(

    @PrimaryKey(true)
    val idPeduliStuntingLocal: Int = 0,
    val idPeduliStuntingServer: Int? = null,
    val bayiLahirPrematur: Int? = null,
    val bayiBblr: Int? = null,
    val balitaStunting: Int? = null,
    val balitaRutinPemeriksaanTumbuhKembang: Int? = null,
    val kehamilanTidakDirencankan: Int? = null,
    val jarakKehamilanTerlaluDekat: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isSynced: Boolean = false

)
