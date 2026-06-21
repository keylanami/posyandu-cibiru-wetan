package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class LogAktivitasLinks(
    val url: String?= null,
    val label: String,
    val active: Boolean
)
