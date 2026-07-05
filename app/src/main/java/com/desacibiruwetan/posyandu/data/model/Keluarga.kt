package com.desacibiruwetan.posyandu.data.model

import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KeluargaReq(

    @Json(name = "rumah_id")
    val rumahId: Int?= null,
    @Json(name = "no_kk")
    val noKK: String,
    @Json(name = "status_kepemilikan_rumah")
    val statusKepemilikanRumah: String,
    @Json(name = "kepemilikan_jamban")
    val kepemilikanJamban: String? = null,
    @Json(name = "kepemilikan_spal")
    val kepemilikanSpal: String? = null,
    @Json(name = "status_ekonomi")
    val statusEkonomi: String,
)


@JsonClass(generateAdapter = true)
data class KeluargaData(
    val id: Int,

    @Json(name = "rumah_id")
    val rumahId: Int,

    @Json(name = "no_kk")
    val noKK: String,

    @Json(name = "status_kepemilikan_rumah")
    val statusKepemilikanRumah: String = "Milik Sendiri",
    @Json(name = "kepemilikan_jamban")
    val kepemilikanJamban: String? = null,
    @Json(name = "kepemilikan_spal")
    val kepemilikanSpal: String? = null,
    @Json(name = "status_ekonomi")
    val statusEkonomi: String = "Sejahtera",

    @Json(name = "created_at")
    val createdAt: String?= null,
    @Json(name = "updated_at")
    val updatedAt: String?= null

)
