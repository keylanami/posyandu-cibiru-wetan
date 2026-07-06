package com.desacibiruwetan.posyandu.utils.update

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ApkInstaller {
    fun install(context: Context, downloadId: Long) {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = manager.query(query)

        if (cursor.moveToFirst()) {
            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (statusIndex != -1 && cursor.getInt(statusIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                if (uriIndex != -1) {
                    val localUriString = cursor.getString(uriIndex)
                    val uri = Uri.parse(localUriString)
                    val file = File(uri.path ?: return)

                    if (file.exists()) {
                        val contentUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(contentUri, "application/vnd.android.package-archive")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
        cursor.close()
    }
}
