package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnggotaReq(

    val nik: String,
    val nama: String,

    @Json(name = "tanggal_lahir")
    val tanggalLahir: String,

    @Json(name = "jenis_kelamin")
    val jenisKelamin: String,

    @Json(name = "pendidikan_terakhir")
    val pendidikanTerakhir: String? = null,

    @Json(name = "no_bpjs")
    val noBpjs: String? = null,

    @Json(name = "status_keluarga")
    val statusKeluarga: String,

    @Json(name = "status_sipil")
    val statusSipil: String,
    val keterangan: String? = null,
)


@JsonClass(generateAdapter = true)
data class AnggotaData(
    val id: Int,
    @Json(name = "keluarga_id")
    val keluargaId: Int,
    val nik: String,
    val nama: String,

    @Json(name = "tanggal_lahir")
    val tanggalLahir: String,

    @Json(name = "jenis_kelamin")
    val jenisKelamin: String,

    @Json(name = "pendidikan_terakhir")
    val pendidikanTerakhir: String? = null,
    val pekerjaan: String? = null,

    @Json(name = "no_bpjs")
    val noBpjs: String? = null,

    @Json(name = "status_keluarga")
    val statusKeluarga: String,

    @Json(name = "status_sipil")
    val statusSipil: String,

    @Json(name = "status_warga")
    val statusWarga: String,

    val keterangan: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null,

    val usia: String,

    @Json(name = "kategori_usia")
    val kategoriUsia: String
)
