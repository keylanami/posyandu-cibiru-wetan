package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppVersion(
    @Json(name = "version_code")
    val versionCode: Int,
    @Json(name = "version_name")
    val versionName: String,
    @Json(name = "minimum_version_code")
    val minimumVersionCode: Int,
    @Json(name = "force_update")
    val forceUpdate: Boolean,
    @Json(name = "apk_url")
    val apkUrl: String,
    val sha256: String,
    @Json(name = "file_size")
    val fileSize: Long,
    @Json(name = "release_notes")
    val releaseNotes: String?,
    @Json(name = "published_at")
    val publishedAt: String
)
