package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeluargaSchema(
    val id: Int,

    @Json(name = "rumah_id")
    val rumahId: Int,

    @Json(name = "no_kk")
    val noKk: String,

    val isNgontrak: Boolean,

    val isGakin: Boolean,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null
)
