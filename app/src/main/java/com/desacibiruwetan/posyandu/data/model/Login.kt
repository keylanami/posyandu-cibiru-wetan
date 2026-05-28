package com.desacibiruwetan.posyandu.data.model

import com.desacibiruwetan.posyandu.data.schema.UserSchema
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginData(
    val user: UserSchema,
    val token: String
)


@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String,
    @Json(name = "device_name")
    val deviceName: String
)

