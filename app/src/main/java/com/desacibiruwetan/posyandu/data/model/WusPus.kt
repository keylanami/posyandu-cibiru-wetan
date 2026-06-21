package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WusPusData(
    val id: Int,

    @Json(name = "anggota_id")
    val anggotaId: Int,

    @Json(name = "nama_suami")
    val namaSuami: String?= null,

    @Json(name = "status_kategori")
    val statusKategori: String,

    @Json(name = "tanggal_mulai_status")
    val tanggalMulaiStatus: String?= null,

    val keterangan: String?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null,

    val kbs: List<KbData>? = null
)


@JsonClass(generateAdapter = true)
data class WusPusReq(

    @Json(name = "nama_suami")
    val namaSuami: String?= null,

    @Json(name = "status_kategori")
    val statusKategori: String,

    @Json(name = "tanggal_mulai_status")
    val tanggalMulaiStatus: String?= null,

    val keterangan: String?= null
)
