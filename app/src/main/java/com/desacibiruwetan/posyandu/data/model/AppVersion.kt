package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppVersion(
    @Json(name = "versionCode")
    val versionCode: Int,
    @Json(name = "versionName")
    val versionName: String,
    @Json(name = "minimumVersionCode")
    val minimumVersionCode: Int,
    @Json(name = "forceUpdate")
    val forceUpdate: Boolean,
    @Json(name = "apkUrl")
    val apkUrl: String,
    val sha256: String,
    @Json(name = "fileSize")
    val fileSize: Long,
    @Json(name = "releaseNotes")
    val releaseNotes: String?,
    @Json(name = "publishedAt")
    val publishedAt: String
)
