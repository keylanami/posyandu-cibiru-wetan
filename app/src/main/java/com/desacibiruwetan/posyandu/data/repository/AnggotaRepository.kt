package com.desacibiruwetan.posyandu.data.repository

import android.util.Log
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.dao.BalitaDao
import com.desacibiruwetan.posyandu.data.local.dao.BumilDao
import com.desacibiruwetan.posyandu.data.local.dao.KbDao
import com.desacibiruwetan.posyandu.data.local.dao.WusPusDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.KbEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.model.BalitaData
import com.desacibiruwetan.posyandu.data.model.BalitaReq
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.BumilReq
import com.desacibiruwetan.posyandu.data.model.KbReq
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
    private val balitaDao: BalitaDao,
    private val bumilDao: BumilDao,
    private val wusPusDao: WusPusDao,
    private val kbDao: KbDao
) {

    fun getAllAnggotaLocal(): Flow<List<AnggotaEntity>> = anggotaDao.getAllAnggotaDao()

    fun getDetailAnggotaPerKeluarga(keluargaId: Int): Flow<List<AnggotaEntity>> =
        anggotaDao.getAnggotaByKeluargaId(keluargaId)

    suspend fun pullDataFromServer(token: String) {
        Log.d(TAG, "pullDataFromServer token=${token.take(15)}...")
        try {
            val response = apiService.getAllAnggota(token)
            Log.d(TAG, "response code=${response.code()} success=${response.isSuccessful}")
            if (response.isSuccessful) {
                val dataServer = response.body()?.data
                Log.d(TAG, "data dari server: ${dataServer?.size ?: "null"} item")
                if (!dataServer.isNullOrEmpty()) {
                    anggotaDao.deleteAllAnggotaLocal()
                }
                dataServer?.forEach { anggotaServer ->
                    val anggotaLokalBaru = AnggotaEntity(
                        serverId = anggotaServer.id,
                        keluargaId = anggotaServer.keluargaId,
                        nik = anggotaServer.nik,
                        nama = anggotaServer.nama,
                        tanggalLahir = anggotaServer.tanggalLahir,
                        jenisKelamin = anggotaServer.jenisKelamin,
                        pendidikanTerakhir = anggotaServer.pendidikanTerakhir,
                        noBpjs = anggotaServer.noBpjs,
                        statusKeluarga = anggotaServer.statusKeluarga,
                        statusSipil = anggotaServer.statusSipil,
                        keterangan = anggotaServer.keterangan,
                        createdAt = anggotaServer.createdAt,
                        updatedAt = anggotaServer.updatedAt,
                        isSynced = true,
                        usia = anggotaServer.usia,
                        kategoriUsia = anggotaServer.kategoriUsia
                    )
                    anggotaDao.insertAnggotaLocal(anggotaLokalBaru)
                }
                Log.d(TAG, "sync selesai, ${dataServer?.size ?: 0} anggota tersimpan")
            } else {
                Log.e(TAG, "response gagal: ${response.code()} ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "exception: ${e.localizedMessage}", e)
        }
    }

    suspend fun addNewAnggota(
        token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String,
        jenisKelamin: String, pendidikanTerakhir: String, pekerjaan: String, noBpjs: String,
        keterangan: String, statusKeluarga: String, statusSipil: String, statusWarga: String,
        usia: String, kategoriUsia: String
    ): Pair<Int, Int?> {
        val entitasBaru = AnggotaEntity(
            keluargaId = keluargaId,
            nik = nik,
            nama = nama,
            tanggalLahir = tanggalLahir,
            jenisKelamin = jenisKelamin,
            pendidikanTerakhir = pendidikanTerakhir,
            pekerjaan = pekerjaan,
            noBpjs = noBpjs,
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
                tanggalLahir = tanggalLahir,
                jenisKelamin = jenisKelamin,
                pendidikanTerakhir = pendidikanTerakhir,
                pekerjaan = pekerjaan,
                noBpjs = noBpjs,
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
        noBpjsBaru: String,
        keteranganBaru: String,
        statusKeluargaBaru: String,
        statusSipilBaru: String,
        statusWarga: String,
        usiaBaru: String,
        kategoriUsiaBaru: String,
    ) {
        val anggotaUpdate = anggotaLokal.copy(
            nik = nikBaru,
            nama = namaBaru,
            tanggalLahir = tanggalLahirBaru,
            jenisKelamin = jenisKelaminBaru,
            pekerjaan = pekerjaanBaru,
            pendidikanTerakhir = pendidikanTerakhirBaru,
            noBpjs = noBpjsBaru,
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
                    tanggalLahir = tanggalLahirBaru,
                    jenisKelamin = jenisKelaminBaru,
                    pendidikanTerakhir = pendidikanTerakhirBaru,
                    pekerjaan = pekerjaanBaru,
                    noBpjs = noBpjsBaru,
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
        namaAyah: String, namaIbu: String, tb: Double, bb: Double
    ) {
        val entitasBalita = BalitaEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            namaAyah = namaAyah,
            namaIbu = namaIbu,
            tinggiBadan = tb,
            beratBadan = bb,
            isSynced = false
        )
        balitaDao.insertBalitaLocal(entitasBalita)

        if (anggotaServerId != null) {
            try {
                val request = BalitaReq(namaAyah, namaIbu, tb, bb)
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
        namaAyah: String,
        namaIbu: String,
        tb: Double,
        bb: Double
    ) {
        val balitaLokal = balitaDao.getBalitaByAnggotaId(anggotaLocalId, anggotaServerId)
        val balitaUpdate = balitaLokal?.copy(
            namaAyah = namaAyah,
            namaIbu = namaIbu,
            tinggiBadan = tb,
            beratBadan = bb,
            isSynced = false
        ) ?: BalitaEntity(
            anggotaLocalId = anggotaLocalId,
            anggotaServerId = anggotaServerId,
            namaAyah = namaAyah,
            namaIbu = namaIbu,
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
                val request = BalitaReq(namaAyah, namaIbu, tb, bb)
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
    ) {
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

    suspend fun deleteDataWusPusById(token: String, wusPusId: Int) {
        try {
            apiService.deleteWusPus(token, wusPusId)
        } catch (e: Exception) {
            Log.e(TAG, "Gagal delete wus pus: ${e.message}")
        }
    }

    suspend fun getDataWusPusById(
        token: String,
        wusPusId: Int
    ): Response<BaseResponse<WusPusData>> {
        return apiService.getWusPusById(token, wusPusId)
    }


    suspend fun updateDataKb(
        token: String, kbLocalId: Int, kbServerId: Int?, wusPusIdServer: Int,
        jenisKb: String, tanggalMulaiKb: String?, statusAktif: Boolean, keterangan: String?, createdAt: String?, updatedAt: String?
    ) {
        val kbLokal = kbDao.getKbById(kbLocalId, kbServerId)

        val kbUpdate = kbLokal?.copy(
            wusPusId = wusPusIdServer, jenisKb = jenisKb, tanggalMulaiKb = tanggalMulaiKb,
            statusAktif = statusAktif, keterangan = keterangan, createdAt = createdAt, updatedAt = updatedAt, isSynced = false
        ) ?: KbEntity(
            wusPusId = wusPusIdServer, jenisKb = jenisKb, tanggalMulaiKb = tanggalMulaiKb,
            statusAktif = statusAktif, keterangan = keterangan, createdAt = createdAt, updatedAt = updatedAt, isSynced = false
        )

        if (kbLokal != null) kbDao.updateKbLocal(kbUpdate)
        else kbDao.insertKbLocal(kbUpdate)

        if (kbUpdate.idKbServer != null) {
            try {
                val req = KbReq(jenisKb, tanggalMulaiKb, statusAktif, keterangan)
                val res = apiService.putKb(token, kbUpdate.idKbServer, req)
                if (res.isSuccessful) kbDao.updateKbLocal(kbUpdate.copy(isSynced = true))
            } catch (e: Exception) {
                Log.e("Repo", "Offline PUT KB")
            }
        } else {
            try {
                val req = KbReq(jenisKb, tanggalMulaiKb, statusAktif, keterangan)
                val res = apiService.postKb(token, wusPusIdServer, req)
                if (res.isSuccessful && res.body()?.data != null) {
                    kbDao.updateKbLocal(
                        kbUpdate.copy(
                            idKbServer = res.body()!!.data!!.id,
                            isSynced = true
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("Repo", "Offline POST KB")
            }
        }
    }


}
