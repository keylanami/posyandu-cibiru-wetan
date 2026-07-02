package com.desacibiruwetan.posyandu.utils

import android.content.Context
import androidx.core.content.edit
import com.desacibiruwetan.posyandu.data.schema.UserSchema

object SessionManager {
    private const val PREFS_NAME = "posyandu_prefs"
    private const val TOKEN_KEY = "TOKEN"
    private const val USER_NAME_KEY = "USER_NAME"
    private const val USER_EMAIL_KEY = "USER_EMAIL"
    private const val USER_PHONE_KEY = "USER_PHONE"
    private const val USER_RT_KEY = "USER_RT"
    private const val USER_RW_KEY = "USER_RW"

    fun getPreferences(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(context: Context, token: String, user: UserSchema) {
        getPreferences(context).edit {
            putString(TOKEN_KEY, stripBearer(token))
            putUserProfile(user)
        }
    }

    fun saveUserProfile(context: Context, user: UserSchema) {
        getPreferences(context).edit {
            putUserProfile(user)
        }
    }

    fun getRawToken(context: Context): String =
        stripBearer(getPreferences(context).getString(TOKEN_KEY, "").orEmpty())

    fun getAuthorizationHeader(context: Context): String =
        formatAuthorizationHeader(getRawToken(context))

    fun formatAuthorizationHeader(token: String): String {
        val rawToken = stripBearer(token)
        return if (rawToken.isBlank()) "" else "Bearer $rawToken"
    }

    fun clearSession(context: Context) {
        getPreferences(context).edit { clear() }
    }

    fun getUserName(context: Context): String =
        getPreferences(context).getString(USER_NAME_KEY, "").orEmpty()

    fun getUserEmail(context: Context): String =
        getPreferences(context).getString(USER_EMAIL_KEY, "").orEmpty()

    fun getUserPhone(context: Context): String =
        getPreferences(context).getString(USER_PHONE_KEY, "").orEmpty()

    fun getUserRt(context: Context): String =
        getPreferences(context).getString(USER_RT_KEY, "").orEmpty()

    fun getUserRw(context: Context): String =
        getPreferences(context).getString(USER_RW_KEY, "").orEmpty()

    private fun android.content.SharedPreferences.Editor.putUserProfile(user: UserSchema) {
        val email = user.email
        val displayName = if (email.contains("@")) email.substringBefore("@") else email
        putString(USER_NAME_KEY, displayName.ifBlank { "Kader" })
        putString(USER_EMAIL_KEY, email)
        putString(USER_PHONE_KEY, user.phoneNumber)
        putString(USER_RT_KEY, user.rt.orEmpty())
        putString(USER_RW_KEY, user.rw.orEmpty())
    }

    private fun stripBearer(token: String): String =
        token.removePrefix("Bearer ").trim()
}
