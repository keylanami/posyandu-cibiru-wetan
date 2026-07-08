package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppVersion(
    @param:Json(name = "versionCode")
    val versionCode: Int,
    @param:Json(name = "versionName")
    val versionName: String,
    @param:Json(name = "minimumVersionCode")
    val minimumVersionCode: Int,
    @param:Json(name = "forceUpdate")
    val forceUpdate: Boolean,
    @param:Json(name = "apkUrl")
    val apkUrl: String,
    val sha256: String,
    @param:Json(name = "fileSize")
    val fileSize: Long,
    @param:Json(name = "releaseNotes")
    val releaseNotes: String?,
    @param:Json(name = "publishedAt")
    val publishedAt: String
)
