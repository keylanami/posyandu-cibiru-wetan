package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PeduliStuntingData(
    val id: Int,

    @param:Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int?= null,

    @param:Json(name = "bayi_bblr")
    val bayiBblr: Int?= null,

    @param:Json(name = "balita_stunting")
    val balitaStunting: Int?= null,

    @param:Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int?= null,

    @param:Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencankan: Int?= null,

    @param:Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int?= null,

    @param:Json(name = "created_at")
    val createdAt: String,

    @param:Json(name = "updated_at")
    val updatedAt: String
)


@JsonClass(generateAdapter = true)
data class PeduliStuntingReq(
    @param:Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int?,

    @param:Json(name = "bayi_bblr")
    val bayiBblr: Int?,

    @param:Json(name = "balita_stunting")
    val balitaStunting: Int?,

    @param:Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int?,

    @param:Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencankan: Int?,

    @param:Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int?

)