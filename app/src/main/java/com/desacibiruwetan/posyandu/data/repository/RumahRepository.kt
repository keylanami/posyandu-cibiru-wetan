package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.dao.SyncStateDao
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.local.entity.SyncStateEntity
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow

private const val TAG = "RumahRepo"


class RumahRepository(
    private val apiService: ApiService,
    private val rumahDao: RumahDao,
    private val syncStateDao: SyncStateDao
){


    fun getAllRumahLocal(): Flow<List<RumahEntity>> {
        return rumahDao.getAllRumahDao()
    }


    // to sync
    suspend fun pullDataFromServer(token: String){
        Log.d(TAG, "pullDataFromServer token=${token.take(15)}...")
        try {
            var cursor: Int? = null
            val updatedSince = syncStateDao.getLastSyncedAt("rumahs")
            var latestUpdatedAt = updatedSince
            var total = 0

            do {
                val response = apiService.getAllRumah(token, limit = 500, cursor = cursor, updatedSince = updatedSince)
                Log.d(TAG, "response code=${response.code()} success=${response.isSuccessful}")
                if (!response.isSuccessful) {
                    Log.e(TAG, "response gagal: ${response.code()} ${response.errorBody()?.string()}")
                    return
                }

                val body = response.body()
                val dataServer = body?.data ?: emptyList()
                total += dataServer.size
                Log.d(TAG, "data rumah dari server: ${dataServer.size} item")

                dataServer.forEach { rumahServer ->
                    val existing = rumahDao.getRumahByServerId(rumahServer.id)
                    if (existing?.isSynced == false) return@forEach

                    val rumahLokalBaru = RumahEntity(
                        localId = existing?.localId ?: 0,
                        serverId = rumahServer.id,
                        rtId = rumahServer.rtId,
                        alamat = rumahServer.alamat,
                        noRumah = rumahServer.nomorRumah,
                        dusun = rumahServer.dusun,
                        createdAt = rumahServer.createdAt,
                        updatedAt = rumahServer.updateAt,
                        isSynced = true
                    )

                    rumahDao.insertRumahLocal(rumahLokalBaru)
                    latestUpdatedAt = newestSyncTime(latestUpdatedAt, rumahServer.updateAt)
                }

                cursor = body?.meta?.nextCursor
            } while (body?.meta?.hasMore == true && cursor != null)

            latestUpdatedAt?.let { syncStateDao.upsert(SyncStateEntity("rumahs", it)) }
            Log.d(TAG, "sync selesai, $total rumah tersimpan")
        } catch(e: Exception){
            Log.e(TAG, "exception: ${e.localizedMessage}", e)
        }
    }


    // to insert or save
    suspend fun addNewRumah(token: String, alamat: String?, dusun: String?, rtId: Int): Long{

        val entitasBaru = RumahEntity(
            rtId = rtId,
            alamat = alamat,
            dusun = dusun,
            isSynced = false
        )

        val localIdBaru = rumahDao.insertRumahLocal(entitasBaru)

        try {
            val request = RumahRequest(
                alamat = alamat,
                dusun = dusun
            )

            val response = apiService.postRumah(token, request)

            if (response.isSuccessful) {
                val dataServer = response.body()?.data

                if (dataServer != null){
                    val rumahSukses = entitasBaru.copy(
                        localId = localIdBaru.toInt(),
                        serverId = dataServer.id,
                        rtId = dataServer.rtId,
                        noRumah = dataServer.nomorRumah,
                        dusun = dataServer.dusun,
                        alamat = dataServer.alamat,
                        isSynced = true,
                        createdAt = dataServer.createdAt,
                        updatedAt = dataServer.updateAt
                    )

                    rumahDao.updateRumahLocal(rumahSukses)
                }
            }

        } catch (e: Exception){
            println("Sedang offline, data rumah disimpan di memori HP dulu. ${e.message}")
        }
        return localIdBaru
    }


    fun getDetailRumah(localId: Int): Flow<RumahEntity>{
        return rumahDao.getRumahById(localId)
    }


    // update
    suspend fun updateRumah(token: String, rumahLokal: RumahEntity, alamatBaru: String?, dusunBaru: String?){
        val rumahUpdate = rumahLokal.copy(
            alamat = alamatBaru,
            dusun = dusunBaru,
            isSynced = false
        )
        rumahDao.updateRumahLocal(rumahUpdate)

        if (rumahUpdate.serverId != null) {
            try {
                val request = RumahRequest(
                    alamat = alamatBaru,
                    dusun = dusunBaru
                )
                val response = apiService.putRumah(token, rumahUpdate.serverId, request)

                if (response.isSuccessful) {
                    val server = response.body()?.data
                    rumahDao.updateRumahLocal(
                        rumahUpdate.copy(
                            serverId = server?.id ?: rumahUpdate.serverId,
                            rtId = server?.rtId ?: rumahUpdate.rtId,
                            noRumah = server?.nomorRumah ?: rumahUpdate.noRumah,
                            dusun = server?.dusun ?: rumahUpdate.dusun,
                            alamat = server?.alamat ?: rumahUpdate.alamat,
                            createdAt = server?.createdAt ?: rumahUpdate.createdAt,
                            updatedAt = server?.updateAt ?: rumahUpdate.updatedAt,
                            isSynced = true
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Update server gagal, tersimpan lokal")
            }
        }
    }
}
