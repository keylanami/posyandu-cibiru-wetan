package com.desacibiruwetan.posyandu.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BumilReq(
    @param:Json(name = "hamil_ke")
    val hamilKe: Int,

    @param:Json(name = "asi_eksklusif")
    val asiEksklusif: Boolean,

    @param:Json(name = "tanggal_mulai_asi")
    val tanggalMulaiAsi: String?= null,

    @param:Json(name = "tanggal_selesai_asi")
    val tanggalSelesaiAsi: String?= null,
)


@JsonClass(generateAdapter = true)
data class BumilData(
    val id: Int,

    @param:Json(name = "anggota_id")
    val anggotaId: Int,

    @param:Json(name = "asi_eksklusif")
    val asiEksklusif: Boolean,

    @param:Json(name = "hamil_ke")
    val hamilKe: Int,

    @param:Json(name = "tanggal_mulai_asi")
    val tanggalMulaiAsi: String?= null,

    @param:Json(name = "tanggal_selesai_asi")
    val tanggalSelesaiAsi: String?= null,

    @param:Json(name = "created_at")
    val createdAt: String?= null,

    @param:Json(name = "updated_at")
    val updatedAt: String?= null

)


