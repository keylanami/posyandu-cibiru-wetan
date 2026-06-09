package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeluargaOpt(
    val id: Int,

    @Json(name = "no_kk")
    val noKk:String,

    @Json(name = "kepala_keluarga")
    val kepalaKeluarga: String?,

)
