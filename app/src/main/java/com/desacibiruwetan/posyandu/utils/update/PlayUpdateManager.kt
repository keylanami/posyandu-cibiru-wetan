package com.desacibiruwetan.posyandu.utils.update

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import kotlinx.coroutines.tasks.await

class PlayUpdateManager(context: Context) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    suspend fun getUpdateInfo(): AppUpdateInfo? {
        return try {
            val info = appUpdateManager.requestAppUpdateInfo()
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                info
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun startUpdate(activity: Activity, info: AppUpdateInfo, isForceUpdate: Boolean) {
        val type = if (isForceUpdate) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE
        
        val options = AppUpdateOptions.newBuilder(type)
            .setAllowAssetPackDeletion(true)
            .build()

        if (type == AppUpdateType.IMMEDIATE && info.isImmediateUpdateAllowed) {
            appUpdateManager.startUpdateFlow(info, activity, options)
        } else if (type == AppUpdateType.FLEXIBLE && info.isFlexibleUpdateAllowed) {
            appUpdateManager.startUpdateFlow(info, activity, options)
        }
    }

    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }
}
