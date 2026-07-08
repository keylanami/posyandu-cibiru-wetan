package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KbData(
    val id: Int,

    @param:Json(name = "wus_pus_id")
    val wusPusId: Int,

    @param:Json(name = "jenis_kb")
    val jenisKb: String,

    @param:Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,

    @param:Json(name = "status_aktif")
    val statusAktif: Boolean,

    val keterangan: String? = null,

    @param:Json(name = "created_at")
    val createdAt: String? = null,

    @param:Json(name = "updated_at")
    val updatedAt: String? = null
)


@JsonClass(generateAdapter = true)
data class KbReq(
    @param:Json(name = "jenis_kb")
    val jenisKb: String,

    @param:Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,

    @param:Json(name = "status_aktif")
    val statusAktif: Boolean,

    val keterangan: String? = null,

    )