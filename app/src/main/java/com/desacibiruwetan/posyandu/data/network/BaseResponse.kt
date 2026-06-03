package com.desacibiruwetan.posyandu.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val errors: Any?= null
)
