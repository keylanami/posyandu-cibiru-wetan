package com.desacibiruwetan.posyandu.utils.update

import android.content.Context
import android.os.Build
import android.util.Log
import com.desacibiruwetan.posyandu.data.model.AppVersion
import com.desacibiruwetan.posyandu.data.network.ApiService

class AppUpdater(
    private val context: Context,
    private val apiService: ApiService
) {
    private companion object {
        const val TAG = "AppUpdater"
    }

    suspend fun checkForUpdate(): AppVersion? {
        return try {
            val response = apiService.getLatestVersion()
            if (response.isSuccessful) {
                val latest = response.body()?.data ?: return null
                val currentVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toInt()
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(context.packageName, 0).versionCode
                }

                if (latest.versionCode > currentVersionCode) {
                    latest
                } else {
                    null
                }
            } else {
                Log.w(TAG, "Update check failed: HTTP ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Update check failed", e)
            null
        }
    }
}
