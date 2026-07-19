package com.desacibiruwetan.posyandu.data.network

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val errors: Any?= null,
    val meta: SyncMeta? = null
)

@JsonClass(generateAdapter = true)
data class SyncMeta(
    val limit: Int? = null,
    @com.squareup.moshi.Json(name = "has_more")
    val hasMore: Boolean? = null,
    @com.squareup.moshi.Json(name = "next_cursor")
    val nextCursor: Int? = null
)
