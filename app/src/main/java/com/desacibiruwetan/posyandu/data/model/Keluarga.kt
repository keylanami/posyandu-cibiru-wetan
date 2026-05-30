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
    val isNgontrak: Boolean,
    val isGakin: Boolean,
)


@JsonClass(generateAdapter = true)
data class KeluargaData(
    val id: Int,

    @Json(name = "rumah_id")
    val rumahId: Int,

    @Json(name = "no_kk")
    val noKK: String,

    val isNgontrak: Boolean,
    val isGakin: Boolean?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,
    @Json(name = "updated_at")
    val updatedAt: String?= null

)
