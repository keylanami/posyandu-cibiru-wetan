package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.dao.BalitaDao
import com.desacibiruwetan.posyandu.data.local.dao.BumilDao
import com.desacibiruwetan.posyandu.data.local.dao.KeluargaDao
import com.desacibiruwetan.posyandu.data.local.dao.SyncStateDao
import com.desacibiruwetan.posyandu.data.local.dao.WusPusDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.SyncStateEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.model.BalitaData
import com.desacibiruwetan.posyandu.data.model.BalitaReq
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.BumilReq
import com.desacibiruwetan.posyandu.data.model.KbData
import com.desacibiruwetan.posyandu.data.model.KbRequest
import com.desacibiruwetan.posyandu.data.model.WusPusData
import com.desacibiruwetan.posyandu.data.model.WusPusReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

private const val TAG = "AnggotaRepo"

class AnggotaRepository(
    private val apiService: ApiService,
    private val anggotaDao: AnggotaDao,
    private val keluargaDao: KeluargaDao,
    private val balitaDao: BalitaDao,
    private val bumilDao: BumilDao,
    private val wusPusDao: WusPusDao,
    private val syncStateDao: SyncStateDao
) {

    fun getAllAnggotaLocal(): Flow<List<AnggotaEntity>> = anggotaDao.getAllAnggotaDao()

    fun getDetailAnggotaPerKeluarga(keluargaId: Int): Flow<List<AnggotaEntity>> =
        anggotaDao.getAnggotaByKeluargaId(keluargaId)

    suspend fun pullDataFromServer(token: String) {
        Log.d(TAG, "pullDataFromServer token=${token.take(15)}...")
        try {
            var cursor: Int? = null
            val updatedSince = syncStateDao.getLastSyncedAt("anggotas")
            var latestUpdatedAt = updatedSince
            var total = 0

            do {
                val response = apiService.getAllAnggota(token, limit = 500, cursor = cursor, updatedSince = updatedSince)
                Log.d(TAG, "response code=${response.code()} success=${response.isSuccessful}")
                if (!response.isSuccessful) {
                    Log.e(TAG, "response gagal: ${response.code()} ${response.errorBody()?.string()}")
                    return
                }

                val body = response.body()
                val dataServer = body?.data ?: emptyList()
                total += dataServer.size
                Log.d(TAG, "data anggota dari server: ${dataServer.size} item")
                dataServer.forEach { anggotaServer ->
                    val keluargaLocal = keluargaDao.getKeluargaByServerId(anggotaServer.keluargaId) ?: return@forEach
                    val existing = anggotaDao.getAnggotaByServerId(anggotaServer.id)
                    if (existing?.isSynced == false) return@forEach

                    val anggotaLokalBaru = AnggotaEntity(
                        localId = existing?.localId ?: 0,
                        serverId = anggotaServer.id,
                        keluargaId = keluargaLocal.localId,
                        nik = anggotaServer.nik,
                        nama = anggotaServer.nama,
                        tempatLahir = anggotaServer.tempatLahir,
                        tanggalLahir = anggotaServer.tanggalLahir,
                        golonganDarah = anggotaServer.golonganDarah,
                        suku = anggotaServer.suku,
                        kewarganegaraan = anggotaServer.kewarganegaraan,
                        jenisKelamin = anggotaServer.jenisKelamin,
                        pendidikanTerakhir = anggotaServer.pendidikanTerakhir,
                        pekerjaan = anggotaServer.pekerjaan,
                        jaminanKesehatan = anggotaServer.jaminanKesehatan,
                        statusKeluarga = anggotaServer.statusKeluarga,
                        statusSipil = anggotaServer.statusSipil,
                        statusWarga = anggotaServer.statusWarga,
                        keterangan = anggotaServer.keterangan,
                        createdAt = anggotaServer.createdAt,
                        updatedAt = anggotaServer.updatedAt,
                        isSynced = true,
                        usia = anggotaServer.usia,
                        kategoriUsia = anggotaServer.kategoriUsia
                    )
                    anggotaDao.insertAnggotaLocal(anggotaLokalBaru)
                    latestUpdatedAt = newestSyncTime(latestUpdatedAt, anggotaServer.updatedAt)
                }

                cursor = body?.meta?.nextCursor
            } while (body?.meta?.hasMore == true && cursor != null)

            latestUpdatedAt?.let { syncStateDao.upsert(SyncStateEntity("anggotas", it)) }
            Log.d(TAG, "sync selesai, $total anggota tersimpan")
        } catch (e: Exception) {
            Log.e(TAG, "exception: ${e.localizedMessage}", e)
        }
    }

    suspend fun addNewAnggota(
        token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String,
        jenisKelamin: String, pendidikanTerakhir: String, pekerjaan: String, jaminanKesehatan: Boolean,
        keterangan: String, statusKeluarga: String, statusSipil: String, statusWarga: String,
        usia: String, kategoriUsia: String,
        tempatLahir: String? = null,
        golonganDarah: String? = null,
        suku: String? = null,
        kewarganegaraan: String = "WNI"
    ): Pair<Int, Int?> {
        val entitasBaru = AnggotaEntity(
            keluargaId = keluargaId,
            nik = nik,
            nama = nama,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            golonganDarah = golonganDarah,
            suku = suku,
            kewarganegaraan = kewarganegaraan,
            jenisKelamin = jenisKelamin,
            pendidikanTerakhir = pendidikanTerakhir,
            pekerjaan = pekerjaan,
            jaminanKesehatan = jaminanKesehatan,
            statusKeluarga = statusKeluarga,
            statusSipil = statusSipil,
            statusWarga = statusWarga,
            keterangan = keterangan,
            usia = usia,
            kategoriUsia = kategoriUsia,
            isSynced = false
        )
        val localIdBaru = anggotaDao.insertAnggotaLocal(entitasBaru).toInt()
        var newServerId: Int? = null

        try {
            val request = AnggotaReq(
                nik = nik,
                nama = nama,
                tempatLahir = tempatLahir,
                tanggalLahir = tanggalLahir,
                golonganDarah = golonganDarah,
                suku = suku,
                kewarganegaraan = kewarganegaraan,
                jenisKelamin = jenisKelamin,
                pendidikanTerakhir = pendidikanTerakhir,
                pekerjaan = pekerjaan,
                jaminanKesehatan = jaminanKesehatan,
                keterangan = keterangan,
                statusWarga = statusWarga,
                statusKeluarga = statusKeluarga,
                statusSipil = statusSipil
            )
            val response = apiService.postAnggota(token, keluargaId, request)
            if (response.isSuccessful && response.body()?.data != null) {
                newServerId = response.body()!!.data!!.id
                anggotaDao.updateAnggotaLocal(
                    entitasBaru.copy(
                        localId = localIdBaru,
                        serverId = newServerId,
                        isSynced = true
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sedang offline, anggota disimpan lokal.")
        }

        return Pair(localIdBaru, newServerId)
    }

    suspend fun updateAnggota(
        token: String,
        anggotaLokal: AnggotaEntity,
        nikBaru: String,
        namaBaru: String,
        tanggalLahirBaru: String,
        jenisKelaminBaru: String,
        pekerjaanBaru: String,
        pendidikanTerakhirBaru: String,
        jaminanKesehatanBaru: Boolean,
        keteranganBaru: String,
        statusKeluargaBaru: String,
        statusSipilBaru: String,
        statusWarga: String,
        usiaBaru: String,
        kategoriUsiaBaru: String,
        tempatLahirBaru: String? = null,
        golonganDarahBaru: String? = null,
        sukuBaru: String? = null,
        kewarganegaraanBaru: String? = null,
    ) {
        val anggotaUpdate = anggotaLokal.copy(
            nik = nikBaru,
            nama = namaBaru,
            tempatLahir = tempatLahirBaru ?: anggotaLokal.tempatLahir,
            tanggalLahir = tanggalLahirBaru,
            golonganDarah = golonganDarahBaru ?: anggotaLokal.golonganDarah,
            suku = sukuBaru ?: anggotaLokal.suku,
            kewarganegaraan = kewarganegaraanBaru ?: anggotaLokal.kewarganegaraan ?: "WNI",
            jenisKelamin = jenisKelaminBaru,
            pekerjaan = pekerjaanBaru,
            pendidikanTerakhir = pendidikanTerakhirBaru,
            jaminanKesehatan = jaminanKesehatanBaru,
            keterangan = keteranganBaru,
            statusKeluarga = statusKeluargaBaru,
            statusSipil = statusSipilBaru,
            statusWarga = statusWarga,
            usia = usiaBaru,
            kategoriUsia = kategoriUsiaBaru,
            isSynced = false
        )
        anggotaDao.updateAnggotaLocal(anggotaUpdate)

        if (anggotaUpdate.serverId != null) {
            try {
                val request = AnggotaReq(
                    nik = nikBaru,
                    nama = namaBaru,
                    tempatLahir = tempatLahirBaru ?: anggotaLokal.tempatLahir,
                    tanggalLahir = tanggalLahirBaru,
                    golonganDarah = golonganDarahBaru ?: anggotaLokal.golonganDarah,
                    suku = sukuBaru ?: anggotaLokal.suku,
                    kewarganegaraan = kewarganegaraanBaru ?: anggotaLokal.kewarganegaraan ?: "WNI",
                    jenisKelamin = jenisKelaminBaru,
                    pendidikanTerakhir = pendidikanTerakhirBaru,
                    pekerjaan = pekerjaanBaru,
                    jaminanKesehatan = jaminanKesehatanBaru,
                    keterangan = keteranganBaru,
                    statusWarga = statusWarga,
                    statusKeluarga = statusKeluargaBaru,
                    statusSipil = statusSipilBaru,
                )
                val response = apiService.putAnggotaId(token, anggotaUpdate.serverId, request)
                if (response.isSuccessful) {
                    anggotaDao.updateAnggotaLocal(anggotaUpdate.copy(isSynced = true))
                }
            } catch (e: Exception) {
                println("Sedang offline, update anggota tersimpan lokal dulu")
            }
        }
    }

    suspend fun addDataBalita(
        token: String, anggotaLocalId: Int, anggotaServerId: Int?,
        tb: Double, bb: Double
    ) {
        val entitasBalita = BalitaEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            tinggiBadan = tb,
            beratBadan = bb,
            isSynced = false
        )
        balitaDao.insertBalitaLocal(entitasBalita)

        if (anggotaServerId != null) {
            try {
                val request = BalitaReq(tb, bb)
                val response = apiService.postBalita(token, anggotaServerId, request)
                if (response.isSuccessful) {
                    balitaDao.updateBalitaLocal(entitasBalita.copy(isSynced = true))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal post balita ke server, disimpan lokal: ${e.message}")
            }
        }
    }

    suspend fun updateDataBalita(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        tb: Double,
        bb: Double
    ) {
        val balitaLokal = balitaDao.getBalitaByAnggotaId(anggotaLocalId, anggotaServerId)
        val balitaUpdate = balitaLokal?.copy(
            tinggiBadan = tb,
            beratBadan = bb,
            isSynced = false
        ) ?: BalitaEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            tinggiBadan = tb,
            beratBadan = bb,
            isSynced = false
        )

        if (balitaLokal != null) {
            balitaDao.updateBalitaLocal(balitaUpdate)
        } else {
            balitaDao.insertBalitaLocal(balitaUpdate)
        }

        if (anggotaServerId != null) {
            try {
                val request = BalitaReq(tb, bb)
                val response = apiService.putBalita(token, anggotaServerId, request)
                if (response.isSuccessful) {
                    balitaDao.updateBalitaLocal(balitaUpdate.copy(isSynced = true))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal update balita ke server, tersimpan lokal: ${e.message}")
            }
        }
    }


    suspend fun getBalitaById(token: String, balitaId: Int): Response<BaseResponse<BalitaData>> {
        return apiService.getBalitaById(token, balitaId)
    }

    suspend fun getBalitaLocalByAnggotaId(localId: Int, serverId: Int?): BalitaEntity? {
        return balitaDao.getBalitaByAnggotaId(localId, serverId)
    }


    suspend fun addDataBumil(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        hamilKe: Int,
        asiEksklusif: Boolean,
        tglMulaiAsi: String?,
        tglSelesaiAsi: String?,
        createdAt: String,
        updatedAt: String
    ) {
        val entitasBumil = BumilEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            hamilKe = hamilKe,
            asiEksklusif = asiEksklusif,
            tanggalMulaiAsi = tglMulaiAsi,
            tanggalSelesaiAsi = tglSelesaiAsi,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isSynced = false
        )
        bumilDao.insertBumilLocal(entitasBumil)

        if (anggotaServerId != null) {
            try {
                val request = BumilReq(hamilKe, asiEksklusif, tglMulaiAsi, tglSelesaiAsi)
                val response = apiService.postbumil(token, anggotaServerId, request)

                if (response.isSuccessful && response.body()?.data != null) {
                    val dataServer = response.body()!!.data!!
                    bumilDao.updateBumilLocal(
                        entitasBumil.copy(
                            bumilServerId = dataServer.id,
                            isSynced = true,
                            createdAt = dataServer.createdAt,
                            updatedAt = dataServer.updatedAt
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal post bumil ke server, tersimpan lokal: ${e.message}")
            }
        }
    }

    suspend fun updateDataBumil(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        hamilKe: Int,
        asiEksklusif: Boolean,
        tglMulaiAsi: String?,
        tglSelesaiAsi: String?,
        createdAt: String,
        updatedAt: String
    ) {
        val bumilLokal = bumilDao.getBumilByAnggotaId(anggotaLocalId, anggotaServerId)

        val bumilUpdate = bumilLokal?.copy(
            hamilKe = hamilKe,
            asiEksklusif = asiEksklusif,
            tanggalMulaiAsi = tglMulaiAsi,
            tanggalSelesaiAsi = tglSelesaiAsi,
            isSynced = false,
            createdAt = createdAt,
            updatedAt = updatedAt
        ) ?: BumilEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            hamilKe = hamilKe,
            asiEksklusif = asiEksklusif,
            tanggalMulaiAsi = tglMulaiAsi,
            tanggalSelesaiAsi = tglSelesaiAsi,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isSynced = false
        )

        if (bumilLokal != null) bumilDao.updateBumilLocal(bumilUpdate)
        else bumilDao.insertBumilLocal(bumilUpdate)


        if (bumilUpdate.bumilServerId != null) {
            try {
                val request = BumilReq(hamilKe, asiEksklusif, tglMulaiAsi, tglSelesaiAsi)
                val response = apiService.putBumil(token, bumilUpdate.bumilServerId, request)
                if (response.isSuccessful) {
                    bumilDao.updateBumilLocal(bumilUpdate.copy(isSynced = true))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal update bumil ke server, tersimpan lokal: ${e.message}")
            }
        } else if (anggotaServerId != null) {
            try {
                val request = BumilReq(hamilKe, asiEksklusif, tglMulaiAsi, tglSelesaiAsi)
                val response = apiService.postbumil(token, anggotaServerId, request)
                if (response.isSuccessful && response.body()?.data != null) {
                    bumilDao.updateBumilLocal(
                        bumilUpdate.copy(
                            bumilServerId = response.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal post fallback bumil: ${e.message}")
            }
        }
    }

    suspend fun getBumilById(token: String, bumilId: Int): Response<BaseResponse<BumilData>> {
        return apiService.getDetailBumilById(token, bumilId)
    }

    suspend fun getBumilsByAnggotaId(token: String, anggotaId: Int): Response<BaseResponse<List<BumilData>>> {
        return apiService.getBumilsByAnggotaId(token, anggotaId)
    }

    suspend fun getBumilLocalByAnggotaId(localId: Int, serverId: Int?): BumilEntity? {
        return bumilDao.getBumilByAnggotaId(localId, serverId)
    }

    suspend fun deleteBumil(token: String, bumilId: Int) {
        try {
            apiService.deleteBumil(token, bumilId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal delete bumil: ${e.message}")
        }
    }


    suspend fun updateDataWusPus(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        namaSuami: String?,
        statusKategori: String,
        tanggalMulaiStatus: String?,
        keterangan: String?,
        createdAt: String,
        updatedAt: String
    ){
        val wusPusLokal = wusPusDao.getWuspusByAnggotaId(anggotaLocalId, anggotaServerId)
        val wusPusUpdate = wusPusLokal?.copy(
            namaSuami = namaSuami,
            statusKategori = statusKategori,
            tanggalMulaiStatus = tanggalMulaiStatus,
            keterangan = keterangan,
            isSynced = false,
            createdAt = createdAt,
            updatedAt = updatedAt
        ) ?: WusPusEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            namaSuami = namaSuami,
            statusKategori = statusKategori,
            tanggalMulaiStatus = tanggalMulaiStatus,
            keterangan = keterangan,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isSynced = false
        )

        if (wusPusLokal != null) wusPusDao.updateWusPusLocal(wusPusUpdate)
        else wusPusDao.insertWusPusLocal(wusPusUpdate)

        if (anggotaServerId != null) {
            try {
                val request = WusPusReq(namaSuami, statusKategori, tanggalMulaiStatus, keterangan)
                val response = apiService.putWusPus(token, anggotaServerId, request)

                if (response.isSuccessful && response.body()?.data != null) {
                    wusPusDao.updateWusPusLocal(
                        wusPusUpdate.copy(
                            wusPusServerId = response.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gagal update WusPus ke server, tersimpan lokal: ${e.message}")
            }
        }
    }

    suspend fun deleteDataWusPusById(token: String, wusPusId: Int){
        try {
            apiService.deleteWusPus(token, wusPusId)
        } catch (e: Exception){
            Log.e(TAG, "Gagal delete wus pus: ${e.message}")
        }
    }

    suspend fun getDataWusPusById(token: String, wusPusId: Int): Response<BaseResponse<WusPusData>>{
        return apiService.getWusPusById(token, wusPusId)
    }

    suspend fun getWusPusLocalByAnggotaId(localId: Int, serverId: Int?): WusPusEntity? {
        return wusPusDao.getWuspusByAnggotaId(localId, serverId)
    }

    suspend fun createKb(
        token: String,
        wusPusId: Int,
        jenisKb: String,
        tanggalMulaiKb: String?,
        statusAktif: Boolean,
        keterangan: String?
    ): Response<BaseResponse<KbData>> {
        return apiService.postKb(
            token = token,
            wusPusId = wusPusId,
            request = KbRequest(
                jenisKb = jenisKb,
                tanggalMulaiKb = tanggalMulaiKb,
                statusAktif = statusAktif,
                keterangan = keterangan
            )
        )
    }
}
