package com.desacibiruwetan.posyandu.utils.update

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

object ApkDownloader {
    fun download(context: Context, url: String, versionName: String): Long {
        val fileName = "posyandu_v$versionName.apk"
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Downloading MyKader Update")
            .setDescription("Version $versionName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return manager.enqueue(request)
    }
}
