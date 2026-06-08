package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow

private const val TAG = "RumahRepo"


class RumahRepository(
    private val apiService: ApiService,
    private val rumahDao: RumahDao
){


    fun getAllRumahLocal(): Flow<List<RumahEntity>> {
        return rumahDao.getAllRumahDao()
    }


    // to sync
    suspend fun pullDataFromServer(token: String){
        Log.d(TAG, "pullDataFromServer token=${token.take(15)}...")
        try {
            val response = apiService.getAllRumah(token)
            Log.d(TAG, "response code=${response.code()} success=${response.isSuccessful}")
            if (response.isSuccessful) {
                val dataServer = response.body()?.data ?: emptyList()
                Log.d(TAG, "data dari server: ${dataServer.size} item")

                dataServer.forEach { rumahServer ->
                    val rumahLokalBaru = RumahEntity(
                        serverId = rumahServer.id,
                        rtId = rumahServer.rtId,
                        alamat = rumahServer.alamat,
                        noRumah = null,
                        createdAt = rumahServer.createdAt,
                        updatedAt = rumahServer.updateAt,
                        isSynced = true
                    )

                    rumahDao.insertRumahLocal(rumahLokalBaru)
                }
                Log.d(TAG, "sync selesai, ${dataServer.size} rumah tersimpan")
            } else {
                Log.e(TAG, "response gagal: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch(e: Exception){
            Log.e(TAG, "exception: ${e.localizedMessage}", e)
        }
    }


    // to insert or save
    suspend fun addNewRumah(token: String, alamat: String, noRumah: String, rtId: Int): Long{

        val entitasBaru = RumahEntity(
            rtId = rtId,
            noRumah = noRumah,
            alamat = alamat,
            isSynced = false
        )

        val localIdBaru = rumahDao.insertRumahLocal(entitasBaru)

        try {
            val request = RumahRequest(
                nomorRumah = noRumah,
                alamat = alamat
            )

            val response = apiService.postRumah(token, request)

            if (response.isSuccessful) {
                val dataServer = response.body()?.data

                if (dataServer != null){
                    val rumahSukses = entitasBaru.copy(
                        localId = localIdBaru.toInt(),
                        serverId = dataServer.id,
                        isSynced = true,
                        createdAt = dataServer.createdAt,
                        updatedAt = dataServer.updateAt
                    )

                    rumahDao.updateRumahLocal(rumahSukses)
                }
            }

        } catch (e: Exception){
            println("Sedang offline, data rumah disimpan di memori HP dulu.")
        }
        return localIdBaru
    }


    fun getDetailRumah(localId: Int): Flow<RumahEntity>{
        return rumahDao.getRumahById(localId)
    }


    // update
    suspend fun updateRumah(token: String, rumahLokal: RumahEntity, alamatBaru: String, noRumahBaru: String){
        val rumahUpdate = rumahLokal.copy(
            alamat = alamatBaru,
            isSynced = false
        )
        rumahDao.updateRumahLocal(rumahUpdate)

        if (rumahUpdate.serverId != null) {
            try {
                val request = RumahRequest(
                    alamat = alamatBaru,
                    nomorRumah = noRumahBaru
                )
                val response = apiService.putRumah(token, rumahUpdate.serverId, request)

                if (response.isSuccessful) {
                    rumahDao.updateRumahLocal(rumahUpdate.copy(isSynced = true))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Update server gagal, tersimpan lokal")
            }
        }
    }
}
