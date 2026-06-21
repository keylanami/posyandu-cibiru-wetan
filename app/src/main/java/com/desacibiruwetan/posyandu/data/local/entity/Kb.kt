package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_kb")
data class KbEntity(
    @PrimaryKey(autoGenerate = true)
    val idKbLocal: Int = 0,
    val idKbServer: Int? = null,
    val wusPusId: Int?= null,
    val jenisKb: String,
    val tanggalMulaiKb: String? = null,
    val statusAktif: Boolean,
    val keterangan: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isSynced: Boolean = false
)
