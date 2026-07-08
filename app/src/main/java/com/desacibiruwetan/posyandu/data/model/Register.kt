package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val nik: String,
    val email: String,

    @param:Json(name = "phone_number")
    val phoneNumber: String,

    val password: String,
    @param:Json(name = "password_confirmation")
    val passwordConfirmation: String,
)


