package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.KeluargaDao
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.model.KeluargaOpt
import com.desacibiruwetan.posyandu.data.model.KeluargaReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow


private const val TAG = "KeluargaRepo"
class KeluargaRepository(
    private val apiService: ApiService,
    private val keluargaDao: KeluargaDao
) {

    fun getAllKeluargaLocal(): Flow<List<KeluargaEntity>>{
        return keluargaDao.getAllKeluargaDao()
    }

    fun getDetailKeluargaPerRumah(rumahId: Int): Flow<List<KeluargaEntity>>{
        return keluargaDao.getKeluargaByRumahId(rumahId)
    }

    suspend fun pullDataFromServer(token: String){
        Log.d(TAG, "pullDataFromServer token=${token.take(15)}...")
        try {
            val response = apiService.getAllKeluarga(token)
            Log.d(TAG, "response code=${response.code()} success=${response.isSuccessful}")

            if (response.isSuccessful){
                val dataServer = response.body()?.data ?: emptyList()
                Log.d(TAG, "data dari server: ${dataServer.size} item")

                dataServer.forEach { keluargaServer ->
                    val keluargaLokalBaru = KeluargaEntity(
                        serverId = keluargaServer.id,
                        rumahId = keluargaServer.rumahId,
                        noKK = keluargaServer.noKK,
                        isNgontrak = keluargaServer.isNgontrak,
                        isGakin = keluargaServer.isGakin,
                        createdAt = keluargaServer.createdAt,
                        updatedAt = keluargaServer.updatedAt,
                        isSynced = true
                    )

                    keluargaDao.insertKeluargaLocal(keluargaLokalBaru)
                }
                Log.d(TAG, "sync selesai, ${dataServer.size} keluarga tersimpan")
            } else {
                Log.e(TAG, "response gagal: ${response.code()} ${response.errorBody()?.string()}")
            }

        } catch (e: Exception){
            println("Gagal tarik server, pakai data lokal: ${e.localizedMessage}")
        }
    }


    suspend fun addNewKeluarga(
        token: String,
        rumahId: Int,
        noKK: String,
        isNgontrak: Boolean,
        isGakin: Boolean,
        noRumahForApi: Int = rumahId
    ): Long{
        val entitasBaru = KeluargaEntity(
            rumahId = rumahId,
            noKK = noKK,
            isNgontrak = isNgontrak,
            isGakin = isGakin,
            isSynced = false
        )

        val localIdBaru = keluargaDao.insertKeluargaLocal(entitasBaru)

        try {
            val request = KeluargaReq(
                noKK = noKK,
                isNgontrak = isNgontrak,
                isGakin = isGakin,
            )

            val response = apiService.postDataKeluarga(token, noRumahForApi, request)

            if (response.isSuccessful){
                val dataServer = response.body()?.data

                if (dataServer!= null){
                    val keluargaSukses = entitasBaru.copy(
                        localId = localIdBaru.toInt(),
                        serverId = dataServer.id,
                        isSynced = true
                    )

                    keluargaDao.updateKeluargaLocal(keluargaSukses)

                }
            }

        } catch (e: Exception){
            println("Sedang offline, data keluarga disimpan di memori HP dulu.")

        }
        return localIdBaru
    }


    suspend fun updateKeluarga(token: String, keluargaLokal: KeluargaEntity, noKKBaru: String, isNgontrakBaru: Boolean, isGakinBaru: Boolean){
        val keluargaUpdate = keluargaLokal.copy(
            noKK = noKKBaru,
            isNgontrak = isNgontrakBaru,
            isGakin = isGakinBaru,
            isSynced = false
        )
        keluargaDao.updateKeluargaLocal(keluargaUpdate)

        try {
            val request = KeluargaReq(
                noKK = noKKBaru,
                isNgontrak = isNgontrakBaru,
                isGakin = isGakinBaru,
                rumahId = keluargaUpdate.rumahId
            )
            val response = apiService.putKeluarga(token, keluargaUpdate.serverId, request)

            if (response.isSuccessful){
                keluargaDao.updateKeluargaLocal(keluargaUpdate.copy(isSynced = true))
            }

        } catch (e: Exception){
            println("Sedang offline, data keluarga disimpan di memori HP dulu.")

        }

    }

    suspend fun getKeluargaOptionsFromServer(token: String): List<KeluargaOpt> {
        try {
            val response = apiService.getKeluargaOption(token)
            if (response.isSuccessful) {
                return response.body()?.data ?: emptyList()
            }
        } catch (e: Exception) {
            println("Gagal mengambil opsi keluarga: ${e.localizedMessage}")
        }
        return emptyList()
    }

}
