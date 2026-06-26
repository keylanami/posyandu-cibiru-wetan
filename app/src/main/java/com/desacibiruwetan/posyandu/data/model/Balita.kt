package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalitaReq(
    @Json(name = "tinggi_badan")
    val tinggiBadan: Double,
    @Json(name = "berat_badan")
    val beratBadan: Double
)

@JsonClass(generateAdapter = true)
data class BalitaData(
    @Json(name = "tinggi_badan")
    val tinggiBadan: Double?,
    @Json(name = "berat_badan")
    val beratBadan: Double?
)
