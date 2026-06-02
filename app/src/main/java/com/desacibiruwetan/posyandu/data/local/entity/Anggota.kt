package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_anggota")
data class AnggotaEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val serverId: Int? = null,
    val keluargaId: Int,
    val nik: String,
    val nama: String,
    val tanggalLahir: String,
    val jenisKelamin: String,
    val pendidikanTerakhir: String?= null,
    val pekerjaan: String?= null,
    val noBpjs: String?= null,
    val statusKeluarga: String,
    val statusSipil: String,
    val statusWarga: String,
    val keterangan: String?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false,
    val usia: String,
    val kategoriUsia: String
)