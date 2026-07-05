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
                        statusKepemilikanRumah = keluargaServer.statusKepemilikanRumah,
                        kepemilikanJamban = keluargaServer.kepemilikanJamban,
                        kepemilikanSpal = keluargaServer.kepemilikanSpal,
                        statusEkonomi = keluargaServer.statusEkonomi,
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
        rumahServerId: Int?,
        noKK: String,
        statusKepemilikanRumah: String,
        kepemilikanJamban: String?,
        kepemilikanSpal: String?,
        statusEkonomi: String
    ): Long{
        val entitasBaru = KeluargaEntity(
            rumahId = rumahId,
            noKK = noKK,
            statusKepemilikanRumah = statusKepemilikanRumah,
            kepemilikanJamban = kepemilikanJamban,
            kepemilikanSpal = kepemilikanSpal,
            statusEkonomi = statusEkonomi,
            isSynced = false
        )

        val localIdBaru = keluargaDao.insertKeluargaLocal(entitasBaru)

        if (rumahServerId == null) {
            return localIdBaru
        }

        try {
            val request = KeluargaReq(
                noKK = noKK,
                statusKepemilikanRumah = statusKepemilikanRumah,
                kepemilikanJamban = kepemilikanJamban,
                kepemilikanSpal = kepemilikanSpal,
                statusEkonomi = statusEkonomi,
            )

            val response = apiService.postDataKeluarga(token, rumahServerId, request)

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


    suspend fun updateKeluarga(
        token: String,
        keluargaLokal: KeluargaEntity,
        noKKBaru: String,
        statusKepemilikanRumahBaru: String,
        kepemilikanJambanBaru: String?,
        kepemilikanSpalBaru: String?,
        statusEkonomiBaru: String
    ){
        val keluargaUpdate = keluargaLokal.copy(
            noKK = noKKBaru,
            statusKepemilikanRumah = statusKepemilikanRumahBaru,
            kepemilikanJamban = kepemilikanJambanBaru,
            kepemilikanSpal = kepemilikanSpalBaru,
            statusEkonomi = statusEkonomiBaru,
            isSynced = false
        )
        keluargaDao.updateKeluargaLocal(keluargaUpdate)

        if (keluargaUpdate.serverId != null) {
            try {
                val request = KeluargaReq(
                    noKK = noKKBaru,
                    statusKepemilikanRumah = statusKepemilikanRumahBaru,
                    kepemilikanJamban = kepemilikanJambanBaru,
                    kepemilikanSpal = kepemilikanSpalBaru,
                    statusEkonomi = statusEkonomiBaru
                )
                val response = apiService.putKeluarga(token, keluargaUpdate.serverId, request)

                if (response.isSuccessful) {
                    val server = response.body()?.data
                    keluargaDao.updateKeluargaLocal(
                        keluargaUpdate.copy(
                            serverId = server?.id ?: keluargaUpdate.serverId,
                            noKK = server?.noKK ?: keluargaUpdate.noKK,
                            statusKepemilikanRumah = server?.statusKepemilikanRumah ?: keluargaUpdate.statusKepemilikanRumah,
                            kepemilikanJamban = server?.kepemilikanJamban ?: keluargaUpdate.kepemilikanJamban,
                            kepemilikanSpal = server?.kepemilikanSpal ?: keluargaUpdate.kepemilikanSpal,
                            statusEkonomi = server?.statusEkonomi ?: keluargaUpdate.statusEkonomi,
                            createdAt = server?.createdAt ?: keluargaUpdate.createdAt,
                            updatedAt = server?.updatedAt ?: keluargaUpdate.updatedAt,
                            isSynced = true
                        )
                    )
                }

            } catch (e: Exception) {
                println("Sedang offline, data keluarga disimpan di memori HP dulu.")

            }
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
