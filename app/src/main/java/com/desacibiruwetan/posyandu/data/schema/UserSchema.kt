package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSchema(
    val id: Int,

    @Json(name = "phone_number")
    val phoneNumber: String,
    val email: String,

    val rt: String? = null,
    val rw: String? = null,

    @Json(name = "email_verified_at")
    val emailVerifiedAt: String?,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String
)
