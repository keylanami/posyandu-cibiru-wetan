package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PeduliStuntingData(
    val id: Int,

    @Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int?= null,

    @Json(name = "bayi_bblr")
    val bayiBblr: Int?= null,

    @Json(name = "balita_stunting")
    val balitaStunting: Int?= null,

    @Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int?= null,

    @Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencankan: Int?= null,

    @Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int?= null,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String
)


@JsonClass(generateAdapter = true)
data class PeduliStuntingReq(
    @Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int?,

    @Json(name = "bayi_bblr")
    val bayiBblr: Int?,

    @Json(name = "balita_stunting")
    val balitaStunting: Int?,

    @Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int?,

    @Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencankan: Int?,

    @Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int?

)