package com.desacibiruwetan.posyandu.data.schema

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSchema(
    val id: Int,

    @param:Json(name = "phone_number")
    val phoneNumber: String,
    val email: String,

    val rt: String? = null,
    val rw: String? = null,

    @param:Json(name = "email_verified_at")
    val emailVerifiedAt: String?,

    @param:Json(name = "created_at")
    val createdAt: String,

    @param:Json(name = "updated_at")
    val updatedAt: String
)
