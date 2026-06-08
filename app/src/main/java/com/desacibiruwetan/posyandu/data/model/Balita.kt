package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalitaReq(
    @Json(name = "nama_ayah") val namaAyah: String,
    @Json(name = "nama_ibu") val namaIbu: String,
    @Json(name = "tinggi_badan") val tinggiBadan: Double,
    @Json(name = "berat_badan") val beratBadan: Double
)