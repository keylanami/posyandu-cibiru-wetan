package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.RiwayatDao
import com.desacibiruwetan.posyandu.data.local.entity.RiwayatEntity
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class RiwayatRepository(
    private val apiService: ApiService,
    private val riwayatDao: RiwayatDao
) {
    fun getRiwayatLokal(): Flow<List<RiwayatEntity>> = riwayatDao.getAllRiwayatLocal()

    suspend fun syncRiwayatFromServer(token: String) {
        try {
            val response = apiService.getActivityLogs(token)
            if (response.isSuccessful) {
                val logItems = response.body()?.data.orEmpty()
                val entitasList = logItems.map { item ->
                    RiwayatEntity(
                        id = item.id,
                        logName = item.logName,
                        description = item.description,
                        subjectType = item.subjectType,
                        subjectId = item.subjectId,
                        event = item.event,
                        causerType = item.causerType,
                        causerId = item.causerId,
                        batchUuid = item.batchUuid,
                        createdAt = item.createdAt,
                        updatedAt = item.updatedAt
                    )
                }

                riwayatDao.deleteAllRiwayatLocal()
                riwayatDao.insertRiwayatLocal(entitasList)
            }
        } catch (e: Exception) {
            Log.e("RiwayatRepo", "Offline, menampilkan riwayat lokal: ${e.message}")
        }
    }
}
