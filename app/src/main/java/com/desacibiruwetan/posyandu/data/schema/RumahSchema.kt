package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RumahSchema(
    val id: Int,
    @Json(name = "rt_id")
    val rtId: Int,

    @Json(name = "nomor_rumah")
    val nomorRumah: String,

    val alamat: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null

)