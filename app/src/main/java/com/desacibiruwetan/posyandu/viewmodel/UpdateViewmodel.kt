package com.desacibiruwetan.posyandu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.model.AppVersion
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.utils.update.ApkDownloader
import com.desacibiruwetan.posyandu.utils.update.ApkInstaller
import com.desacibiruwetan.posyandu.utils.update.AppUpdater
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateViewmodel(
    application: Application,
    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val _updateInfo = MutableStateFlow<AppVersion?>(null)
    val updateInfo = _updateInfo.asStateFlow()

    private val _downloadId = MutableStateFlow<Long?>(null)
    val downloadId = _downloadId.asStateFlow()

    fun checkForUpdate() {
        viewModelScope.launch {
            val updater = AppUpdater(getApplication(), apiService)
            _updateInfo.value = updater.checkForUpdate()
        }
    }

    fun startDownload() {
        val info = _updateInfo.value ?: return
        val id = ApkDownloader.download(getApplication(), info.apkUrl, info.versionName)
        _downloadId.value = id
    }

    fun installUpdate() {
        val id = _downloadId.value ?: return
        ApkInstaller.install(getApplication(), id)
    }

    fun clearUpdateInfo() {
        _updateInfo.value = null
    }
}
