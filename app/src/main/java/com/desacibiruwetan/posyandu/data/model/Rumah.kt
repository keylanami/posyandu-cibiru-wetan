package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RumahRequest(
    val alamat: String? = null,
    val dusun: String? = null
)


@JsonClass(generateAdapter = true)
data class RumahData(
    val id: Int,
    @param:Json(name = "no_rumah")
    val nomorRumah: String,


    @param:Json(name = "rt_id")
    val rtId: Int,


    val dusun: String?= null,

    val alamat: String?= null,

    @param:Json(name = "created_at")
    val createdAt: String?= null,

    @param:Json(name = "updated_at")
    val updateAt: String?= null
)
