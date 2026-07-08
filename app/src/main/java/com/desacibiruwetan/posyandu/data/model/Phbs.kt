package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PhbsData(
    val id: Int,

    @param:Json(name = "patuh_protokol_kesehatan")
    val patuhProtokolKesehatan: Int?= null,

    @param:Json(name = "rumah_jamban_sehat")
    val rumahJambanSehat: Int?= null,

    @param:Json(name = "rumah_air_bersih")
    val rumahAirBersih: Int?= null,

    @param:Json(name = "kasus_diare")
    val kasusDiare: Int?= null,

    @param:Json(name = "keluarga_sadar_gizi")
    val keluargaSadarGizi: Int?= null,

    @param:Json(name = "rumah_tanpa_asap_rokok")
    val rumahTanpaAsapRokok: Int?= null,

    val babs: Int?= null,

    @param:Json(name = "created_at")
    val createdAt: String,

    @param:Json(name = "updated_at")
    val updatedAt: String
)



@JsonClass(generateAdapter = true)
data class PhbsReq(
    @param:Json(name = "patuh_protokol_kesehatan")
    val patuhProtokolKesehatan: Int?= null,

    @param:Json(name = "rumah_jamban_sehat")
    val rumahJambanSehat: Int?= null,

    @param:Json(name = "rumah_air_bersih")
    val rumahAirBersih: Int?= null,

    @param:Json(name = "kasus_diare")
    val kasusDiare: Int?= null,

    @param:Json(name = "keluarga_sadar_gizi")
    val keluargaSadarGizi: Int?= null,

    @param:Json(name = "rumah_tanpa_asap_rokok")
    val rumahTanpaAsapRokok: Int?= null,

    val babs: Int?= null

)
