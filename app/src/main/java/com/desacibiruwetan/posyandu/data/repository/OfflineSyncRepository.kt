package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.database.AppDatabase
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.model.BalitaReq
import com.desacibiruwetan.posyandu.data.model.BumilReq
import com.desacibiruwetan.posyandu.data.model.KeluargaReq
import com.desacibiruwetan.posyandu.data.model.RumahRequest
import com.desacibiruwetan.posyandu.data.model.WusPusReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val OFFLINE_SYNC_TAG = "OfflineSync"

class OfflineSyncRepository(
    private val apiService: ApiService,
    private val database: AppDatabase
) {
    private val mutex = Mutex()
    private val rumahDao = database.rumahDao()
    private val keluargaDao = database.keluargaDao()
    private val anggotaDao = database.anggotaDao()
    private val balitaDao = database.balitaDao()
    private val bumilDao = database.bumilDao()
    private val wusPusDao = database.wusPusDao()

    suspend fun syncPendingChanges(token: String): Boolean = mutex.withLock {
        if (token.isBlank()) return@withLock false

        return@withLock runCatching {
            syncRumah(token)
            syncKeluarga(token)
            syncAnggota(token)
            syncBalita(token)
            syncBumil(token)
            syncWusPus(token)
            true
        }.onFailure {
            Log.e(OFFLINE_SYNC_TAG, "Sync pending gagal: ${it.localizedMessage}", it)
        }.getOrDefault(false)
    }

    private suspend fun syncRumah(token: String) {
        rumahDao.getRumahBelumSinkron().forEach { rumah ->
            val request = RumahRequest(
                alamat = rumah.alamat.orEmpty()
            )

            val response = if (rumah.serverId == null) {
                apiService.postRumah(token, request)
            } else {
                apiService.putRumah(token, rumah.serverId, request)
            }

            if (response.isSuccessful) {
                val server = response.body()?.data
                rumahDao.updateRumahLocal(
                    rumah.copy(
                        serverId = server?.id ?: rumah.serverId,
                        rtId = server?.rtId ?: rumah.rtId,
                        noRumah = server?.nomorRumah ?: rumah.noRumah,
                        createdAt = server?.createdAt ?: rumah.createdAt,
                        updatedAt = server?.updateAt ?: rumah.updatedAt,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun syncKeluarga(token: String) {
        keluargaDao.getKeluargaBelumSinkron().forEach { keluarga ->
            val rumah = rumahDao.getRumahByLocalOrServerId(keluarga.rumahId)
            val rumahServerId = rumah?.serverId ?: return@forEach

            val request = KeluargaReq(
                noKK = keluarga.noKK,
                isNgontrak = keluarga.isNgontrak,
                isGakin = keluarga.isGakin ?: false
            )

            val response = if (keluarga.serverId == null) {
                apiService.postDataKeluarga(token, rumahServerId, request)
            } else {
                apiService.putKeluarga(token, keluarga.serverId, request)
            }

            if (response.isSuccessful) {
                val server = response.body()?.data
                keluargaDao.updateKeluargaLocal(
                    keluarga.copy(
                        serverId = server?.id ?: keluarga.serverId,
                        rumahId = rumah.localId,
                        createdAt = server?.createdAt ?: keluarga.createdAt,
                        updatedAt = server?.updatedAt ?: keluarga.updatedAt,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun syncAnggota(token: String) {
        anggotaDao.getAnggotaBelumSinkron().forEach { anggota ->
            val keluarga = keluargaDao.getKeluargaByLocalOrServerId(anggota.keluargaId) ?: return@forEach
            val keluargaServerId = keluarga.serverId ?: return@forEach
            val request = anggota.toRequest()

            val response = if (anggota.serverId == null) {
                apiService.postAnggota(token, keluargaServerId, request)
            } else {
                apiService.putAnggotaId(token, anggota.serverId, request)
            }

            if (response.isSuccessful) {
                val server = response.body()?.data
                anggotaDao.updateAnggotaLocal(
                    anggota.copy(
                        serverId = server?.id ?: anggota.serverId,
                        keluargaId = keluarga.localId,
                        createdAt = server?.createdAt ?: anggota.createdAt,
                        updatedAt = server?.updatedAt ?: anggota.updatedAt,
                        usia = server?.usia ?: anggota.usia,
                        kategoriUsia = server?.kategoriUsia ?: anggota.kategoriUsia,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun syncBalita(token: String) {
        balitaDao.getBalitaBelumSync().forEach { balita ->
            val anggota = findAnggotaForProgram(balita.anggotaLocalId, balita.anggotaServerId) ?: return@forEach
            val anggotaServerId = anggota.serverId ?: return@forEach
            val request = BalitaReq(balita.tinggiBadan, balita.beratBadan)

            val putResponse = apiService.putBalita(token, anggotaServerId, request)
            val success = putResponse.isSuccessful || apiService.postBalita(token, anggotaServerId, request).isSuccessful
            if (success) {
                balitaDao.updateBalitaLocal(
                    balita.copy(
                        anggotaLocalId = anggota.localId,
                        anggotaServerId = anggotaServerId,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun syncBumil(token: String) {
        bumilDao.getBumilBelumSync().forEach { bumil ->
            val anggota = findAnggotaForProgram(bumil.anggotaLocalId, bumil.anggotaServerId) ?: return@forEach
            val anggotaServerId = anggota.serverId ?: return@forEach
            val request = BumilReq(bumil.hamilKe, bumil.asiEksklusif, bumil.tanggalMulaiAsi, bumil.tanggalSelesaiAsi)

            val response = if (bumil.bumilServerId == null) {
                apiService.postbumil(token, anggotaServerId, request)
            } else {
                apiService.putBumil(token, bumil.bumilServerId, request)
            }

            if (response.isSuccessful) {
                val server = response.body()?.data
                bumilDao.updateBumilLocal(
                    bumil.copy(
                        anggotaLocalId = anggota.localId,
                        anggotaServerId = anggotaServerId,
                        bumilServerId = server?.id ?: bumil.bumilServerId,
                        createdAt = server?.createdAt ?: bumil.createdAt,
                        updatedAt = server?.updatedAt ?: bumil.updatedAt,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun syncWusPus(token: String) {
        wusPusDao.getWusPusBelumSinkron().forEach { wusPus ->
            val anggota = findAnggotaForProgram(wusPus.anggotaLocalId, wusPus.anggotaServerId) ?: return@forEach
            val anggotaServerId = anggota.serverId ?: return@forEach
            val request = WusPusReq(
                namaSuami = wusPus.namaSuami,
                statusKategori = wusPus.statusKategori,
                tanggalMulaiStatus = wusPus.tanggalMulaiStatus,
                keterangan = wusPus.keterangan
            )

            val response = if (wusPus.wusPusServerId == null) {
                apiService.postWusPus(token, anggotaServerId, request)
            } else {
                apiService.putWusPus(token, anggotaServerId, request)
            }

            if (response.isSuccessful) {
                val server = response.body()?.data
                wusPusDao.updateWusPusLocal(
                    wusPus.copy(
                        anggotaLocalId = anggota.localId,
                        anggotaServerId = anggotaServerId,
                        wusPusServerId = server?.id ?: wusPus.wusPusServerId,
                        createdAt = server?.createdAt ?: wusPus.createdAt,
                        updatedAt = server?.updatedAt ?: wusPus.updatedAt,
                        isSynced = true
                    )
                )
            }
        }
    }

    private suspend fun findAnggotaForProgram(localId: Int, serverId: Int?): AnggotaEntity? {
        return anggotaDao.getAnggotaByLocalOrServerId(localId)
            ?: serverId?.let { anggotaDao.getAnggotaByLocalOrServerId(it) }
    }

    private fun AnggotaEntity.toRequest(): AnggotaReq {
        return AnggotaReq(
            nik = nik,
            nama = nama,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            golonganDarah = golonganDarah,
            suku = suku,
            kewarganegaraan = kewarganegaraan ?: "WNI",
            jenisKelamin = jenisKelamin,
            pendidikanTerakhir = pendidikanTerakhir,
            noBpjs = noBpjs,
            statusKeluarga = statusKeluarga,
            statusSipil = statusSipil,
            statusWarga = statusWarga ?: "aktif",
            pekerjaan = pekerjaan,
            keterangan = keterangan
        )
    }
}
