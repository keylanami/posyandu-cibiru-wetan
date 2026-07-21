package com.desacibiruwetan.posyandu.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.model.AppVersion
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.utils.update.AppUpdater
import com.desacibiruwetan.posyandu.utils.update.PlayUpdateManager
import com.google.android.play.core.appupdate.AppUpdateInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateViewmodel(
    application: Application,
    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val playUpdateManager = PlayUpdateManager(application)

    private val _updateInfo = MutableStateFlow<AppVersion?>(null)
    val updateInfo = _updateInfo.asStateFlow()

    private val _playUpdateInfo = MutableStateFlow<AppUpdateInfo?>(null)
    val playUpdateInfo = _playUpdateInfo.asStateFlow()

    fun checkForUpdate() {
        viewModelScope.launch {
            // 1. Check our own API for force update info and release notes
            val updater = AppUpdater(getApplication(), apiService)
            val info = updater.checkForUpdate()
            _updateInfo.value = info

            // 2. Check Google Play for actual update availability
            if (info != null) {
                _playUpdateInfo.value = playUpdateManager.getUpdateInfo()
            }
        }
    }

    fun startPlayUpdate(activity: Activity) {
        val playInfo = _playUpdateInfo.value ?: return
        val info = _updateInfo.value ?: return
        
        playUpdateManager.startUpdate(
            activity = activity,
            info = playInfo,
            isForceUpdate = info.forceUpdate
        )
    }

    fun completeUpdate() {
        playUpdateManager.completeUpdate()
    }

    fun clearUpdateInfo() {
        _updateInfo.value = null
        _playUpdateInfo.value = null
    }
}
