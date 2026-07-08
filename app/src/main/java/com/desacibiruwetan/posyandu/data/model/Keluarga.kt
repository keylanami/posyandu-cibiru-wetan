package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KeluargaReq(

    @param:Json(name = "rumah_id")
    val rumahId: Int?= null,
    @param:Json(name = "no_kk")
    val noKK: String,
    @param:Json(name = "status_kepemilikan_rumah")
    val statusKepemilikanRumah: String,
    @param:Json(name = "kepemilikan_jamban")
    val kepemilikanJamban: String? = null,
    @param:Json(name = "kepemilikan_spal")
    val kepemilikanSpal: String? = null,
    @param:Json(name = "status_ekonomi")
    val statusEkonomi: String,
)


@JsonClass(generateAdapter = true)
data class KeluargaData(
    val id: Int,

    @param:Json(name = "rumah_id")
    val rumahId: Int,

    @param:Json(name = "no_kk")
    val noKK: String,

    @param:Json(name = "status_kepemilikan_rumah")
    val statusKepemilikanRumah: String = "Milik Sendiri",
    @param:Json(name = "kepemilikan_jamban")
    val kepemilikanJamban: String? = null,
    @param:Json(name = "kepemilikan_spal")
    val kepemilikanSpal: String? = null,
    @param:Json(name = "status_ekonomi")
    val statusEkonomi: String = "Sejahtera",

    @param:Json(name = "created_at")
    val createdAt: String?= null,
    @param:Json(name = "updated_at")
    val updatedAt: String?= null

)
