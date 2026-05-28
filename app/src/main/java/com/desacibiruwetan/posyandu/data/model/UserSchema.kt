package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSchema(
    val id: Int,
    val name: String,

    @Json(name = "phone_number")
    val phoneNumber: String,
    val email: String,

    @Json(name = "email_verified_at")
    val emailVerifiedAt: String?,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String
)
