package com.desacibiruwetan.posyandu.data.repository

import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow


class RumahRepository(
    private val apiService: ApiService,
    private val rumahDao: RumahDao
){


    fun getAllRumahLocal(): Flow<List<RumahEntity>> {
        return rumahDao.getAllRumahDao()
    }


    // to sync
    suspend fun pullDataFromServer(token: String){
        try {
            val response = apiService.getAllRumah(token)
            if (response.isSuccessful) {
                val dataServer = response.body()?.data ?: emptyList()

                dataServer.forEach { rumahServer ->
                    val rumahLokalBaru = RumahEntity(
                        serverId = rumahServer.id,
                        rtId = rumahServer.rtId,
                        noRumah = rumahServer.nomorRumah,
                        alamat = rumahServer.alamat,
                        createdAt = rumahServer.createdAt,
                        updatedAt = rumahServer.updateAt,
                        isSynced = true
                    )

                    rumahDao.insertRumahLocal(rumahLokalBaru)

                }
            }

        } catch(e: Exception){
            println("Gagal tarik server, pakai data lokal: ${e.localizedMessage}")
        }
    }


    // to insert or save
    suspend fun addNewRumah(token: String, alamat: String, noRumah: String){

        val entitasBaru = RumahEntity(
            noRumah = noRumah,
            alamat = alamat,
            isSynced = true
        )

        val localIdBaru = rumahDao.insertRumahLocal(entitasBaru)

        try {
            val request = RumahRequest(
                noRumah = noRumah,
                alamat = alamat
            )

            val response = apiService.postRumah(token, request)

            if (response.isSuccessful) {
                val dataServer = response.body()?.data

                if (dataServer != null){
                    val rumahSukses = entitasBaru.copy(
                        localId = localIdBaru.toInt(),
                        serverId = dataServer.id,
                        isSynced = true
                    )

                    rumahDao.updateRumahLocal(rumahSukses)
                }
            }

        } catch (e: Exception){
            println("Sedang offline, data rumah disimpan di memori HP dulu.")
        }

    }


    fun getDetailRumah(localId: Int): Flow<RumahEntity>{
        return rumahDao.getRumahById(localId)
    }


    // update
    suspend fun updateRumah(token: String, rumahLokal: RumahEntity, alamatBaru: String, noRumahBaru: String){
        val rumahUpdate = rumahLokal.copy(
            alamat = alamatBaru,
            noRumah = noRumahBaru,
            isSynced = true
        )
        rumahDao.updateRumahLocal(rumahUpdate)

        try {
            val request = RumahRequest(alamat = alamatBaru, noRumah = noRumahBaru)
            val response = apiService.putRumah(token, rumahUpdate.serverId, request)

        } catch (e: Exception){
            println("Sedang offline, data rumah disimpan di memori HP dulu.")
        }
    }
}
