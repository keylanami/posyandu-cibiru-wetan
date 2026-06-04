package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RumahRequest(
    val alamat: String,

    @Json(name = "no_rumah")
    val noRumah: String,
)


@JsonClass(generateAdapter = true)
data class RumahData(
    val id: Int,

    @Json(name = "rt_id")
    val rtId: Int,

    @Json(name = "no_rumah")
    val nomorRumah: String,

    val alamat: String?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updateAt: String?= null
)