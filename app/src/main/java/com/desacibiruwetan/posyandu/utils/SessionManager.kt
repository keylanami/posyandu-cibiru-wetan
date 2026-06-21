package com.desacibiruwetan.posyandu.utils

import android.content.Context
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "posyandu_prefs"
    private const val TOKEN_KEY = "TOKEN"
    private const val USER_RT_KEY = "USER_RT"
    private const val USER_RW_KEY = "USER_RW"

    fun getPreferences(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(context: Context, token: String, rt: String?, rw: String?) {
        getPreferences(context).edit {
            putString(TOKEN_KEY, stripBearer(token))
            putString(USER_RT_KEY, rt.orEmpty())
            putString(USER_RW_KEY, rw.orEmpty())
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

    private fun stripBearer(token: String): String =
        token.removePrefix("Bearer ").trim()
}
