package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KbData(
    val id: Int,

    @Json(name = "wus_pus_id")
    val wusPusId: Int,

    @Json(name = "jenis_kb")
    val jenisKb: String,

    @Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,

    @Json(name = "status_aktif")
    val statusAktif: Boolean,

    val keterangan: String? = null,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null
)


@JsonClass(generateAdapter = true)
data class KbReq(
    @Json(name = "jenis_kb")
    val jenisKb: String,

    @Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,

    @Json(name = "status_aktif")
    val statusAktif: Boolean,

    val keterangan: String? = null,

)