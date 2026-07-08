package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalitaReq(
    @param:Json(name = "tinggi_badan")
    val tinggiBadan: Double,
    @param:Json(name = "berat_badan")
    val beratBadan: Double
)

@JsonClass(generateAdapter = true)
data class BalitaData(
    @param:Json(name = "tinggi_badan")
    val tinggiBadan: Double?,
    @param:Json(name = "berat_badan")
    val beratBadan: Double?
)
