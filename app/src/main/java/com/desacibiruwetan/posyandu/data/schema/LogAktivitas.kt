package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogAktivitas(
    val id: Int,

    @Json(name = "log_name")
    val logName: String?= null,
    val description: String,

    @Json(name = "subject_type")
    val subjectType: String?= null,
    val event: String?= null,

    @Json(name = "subject_id")
    val subjectId: Int?= null,

    @Json(name = "causer_type")
    val causerType: String?= null,

    @Json(name = "causer_id")
    val causerId: Int?= null,

    val properties: Any?= null,

    @Json(name = "batch_uuid")
    val batchUuid: String?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null
)
