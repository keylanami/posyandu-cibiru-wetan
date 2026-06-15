package com.desacibiruwetan.posyandu.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BumilReq(
    @Json(name = "hamil_ke")
    val hamilKe: Int,

    @Json(name = "asi_eksklusif")
    val asiEksklusif: Boolean,

    @Json(name = "tanggal_mulai_asi")
    val tanggalMulaiAsi: String?= null,

    @Json(name = "tanggal_selesai_asi")
    val tanggalSelesaiAsi: String?= null,
)


@JsonClass(generateAdapter = true)
data class BumilData(
    val id: Int,

    @Json(name = "anggota_id")
    val anggotaId: Int,

    @Json(name = "asi_eksklusif")
    val asiEksklusif: Boolean,

    @Json(name = "hamil_ke")
    val hamilKe: Int,

    @Json(name = "tanggal_mulai_asi")
    val tanggalMulaiAsi: String?= null,

    @Json(name = "tanggal_selesai_asi")
    val tanggalSelesaiAsi: String?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null

)


