package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnggotaReq(

    val nik: String,
    val nama: String,

    @param:Json(name = "tempat_lahir")
    val tempatLahir: String? = null,

    @param:Json(name = "tanggal_lahir")
    val tanggalLahir: String,

    @param:Json(name = "golongan_darah")
    val golonganDarah: String? = null,

    val suku: String? = null,

    val kewarganegaraan: String = "WNI",

    @param:Json(name = "jenis_kelamin")
    val jenisKelamin: String,

    @param:Json(name = "pendidikan_terakhir")
    val pendidikanTerakhir: String? = null,

    @param:Json(name = "jaminan_kesehatan")
    val jaminanKesehatan: Boolean = false,

    @param:Json(name = "status_keluarga")
    val statusKeluarga: String,

    @param:Json(name = "status_sipil")
    val statusSipil: String,

    @param:Json(name = "status_warga")
    val statusWarga: String,

    val pekerjaan: String?= null,
    val keterangan: String? = null,
)


@JsonClass(generateAdapter = true)
data class AnggotaData(
    val id: Int,
    @param:Json(name = "keluarga_id")
    val keluargaId: Int,
    val nik: String,
    val nama: String,

    @param:Json(name = "tempat_lahir")
    val tempatLahir: String? = null,

    @param:Json(name = "tanggal_lahir")
    val tanggalLahir: String,

    @param:Json(name = "golongan_darah")
    val golonganDarah: String? = null,

    val suku: String? = null,

    val kewarganegaraan: String? = "WNI",

    @param:Json(name = "jenis_kelamin")
    val jenisKelamin: String,

    @param:Json(name = "pendidikan_terakhir")
    val pendidikanTerakhir: String? = null,
    val pekerjaan: String? = null,

    @param:Json(name = "jaminan_kesehatan")
    val jaminanKesehatan: Boolean = false,

    @param:Json(name = "status_keluarga")
    val statusKeluarga: String,

    @param:Json(name = "status_sipil")
    val statusSipil: String,

    @param:Json(name = "status_warga")
    val statusWarga: String,

    val keterangan: String? = null,

    @param:Json(name = "created_at")
    val createdAt: String? = null,

    @param:Json(name = "updated_at")
    val updatedAt: String? = null,

    val usia: String,

    @param:Json(name = "kategori_usia")
    val kategoriUsia: String
)
