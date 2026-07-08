package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WusPusData(
    val id: Int,

    @param:Json(name = "anggota_id")
    val anggotaId: Int,

    @param:Json(name = "nama_suami")
    val namaSuami: String?= null,

    @param:Json(name = "status_kategori")
    val statusKategori: String,

    @param:Json(name = "tanggal_mulai_status")
    val tanggalMulaiStatus: String?= null,

    val keterangan: String?= null,

    @param:Json(name = "created_at")
    val createdAt: String?= null,

    @param:Json(name = "updated_at")
    val updatedAt: String?= null,

    val kbs: List<KbData>? = null
)


@JsonClass(generateAdapter = true)
data class WusPusReq(

    @param:Json(name = "nama_suami")
    val namaSuami: String?= null,

    @param:Json(name = "status_kategori")
    val statusKategori: String,

    @param:Json(name = "tanggal_mulai_status")
    val tanggalMulaiStatus: String?= null,

    val keterangan: String?= null
)
