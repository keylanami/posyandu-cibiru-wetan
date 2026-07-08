package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogAktivitas(
    val id: Int,

    @param:Json(name = "log_name")
    val logName: String?= null,
    val description: String,

    @param:Json(name = "subject_type")
    val subjectType: String?= null,
    val event: String?= null,

    @param:Json(name = "subject_id")
    val subjectId: Int?= null,

    @param:Json(name = "causer_type")
    val causerType: String?= null,

    @param:Json(name = "causer_id")
    val causerId: Int?= null,

    val properties: Any?= null,

    @param:Json(name = "batch_uuid")
    val batchUuid: String?= null,

    @param:Json(name = "created_at")
    val createdAt: String?= null,

    @param:Json(name = "updated_at")
    val updatedAt: String?= null
)
