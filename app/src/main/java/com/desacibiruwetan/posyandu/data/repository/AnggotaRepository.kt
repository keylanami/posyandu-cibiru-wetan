package com.desacibiruwetan.posyandu.data.repository

import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class AnggotaRepository(
    private val apiService: ApiService, private val anggotaDao: AnggotaDao
) {

    fun getAllAnggotaLocal(): Flow<List<AnggotaEntity>> = anggotaDao.getAllAnggotaDao()

    fun getDetailAnggotaPerKeluarga(keluargaId: Int): Flow<List<AnggotaEntity>> =
        anggotaDao.getAnggotaByKeluargaId(keluargaId)

    suspend fun pullDataFromServer(token: String) {
        try {
            val response = apiService.getAllAnggota(token)
            if (response.isSuccessful) {
                val dataServer = response.body()?.data ?: emptyList()
                dataServer.forEach { anggotaServer ->
                    val anggotaLokalBaru = AnggotaEntity(
                        serverId = anggotaServer.id,
                        keluargaId = anggotaServer.keluargaId,
                        nik = anggotaServer.nik,
                        nama = anggotaServer.nama,
                        tanggalLahir = anggotaServer.tanggalLahir,
                        jenisKelamin = anggotaServer.jenisKelamin,
                        pendidikanTerakhir = anggotaServer.pendidikanTerakhir,
                        pekerjaan = anggotaServer.pekerjaan,
                        noBpjs = anggotaServer.noBpjs,
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
                }
            }
        } catch (e: Exception) {
            println("Gagal tarik data anggota dari server: ${e.localizedMessage}")
        }
    }

    suspend fun addNewAnggota(
        token: String,
        keluargaId: Int,
        nik: String,
        nama: String,
        tanggalLahir: String,
        jenisKelamin: String,
        pendidikanTerakhir: String,
        noBpjs: String,
        keterangan: String,
        statusKeluarga: String,
        statusSipil: String,
        statusWarga: String,
        usia: String,
        kategoriUsia: String
    ) {
        val entitasBaru = AnggotaEntity(
            keluargaId = keluargaId,
            nik = nik,
            nama = nama,
            tanggalLahir = tanggalLahir,
            jenisKelamin = jenisKelamin,
            pendidikanTerakhir = pendidikanTerakhir,
            noBpjs = noBpjs,
            statusKeluarga = statusKeluarga,
            statusSipil = statusSipil,
            statusWarga = statusWarga,
            keterangan = keterangan,
            usia = usia,
            kategoriUsia = kategoriUsia,
            isSynced = false
        )
        val localIdBaru = anggotaDao.insertAnggotaLocal(entitasBaru)

        try {
            val request = AnggotaReq(
                nik = nik,
                nama = nama,
                tanggalLahir = tanggalLahir,
                jenisKelamin = jenisKelamin,
                pendidikanTerakhir = pendidikanTerakhir,
                noBpjs = noBpjs,
                keterangan = keterangan,
                statusKeluarga = statusKeluarga,
                statusSipil = statusSipil
            )
            val response = apiService.postAnggota(token, keluargaId, request)

            if (response.isSuccessful && response.body()?.data != null) {
                val dataServer = response.body()!!.data!!
                val anggotaSukses = entitasBaru.copy(
                    localId = localIdBaru.toInt(), serverId = dataServer.id, isSynced = true
                )
                anggotaDao.updateAnggotaLocal(anggotaSukses)
            }
        } catch (e: Exception) {
            println("Sedang offline, data anggota disimpan lokal dulu")
        }
    }

    suspend fun updateAnggota(
        token: String,
        anggotaLokal: AnggotaEntity,
        nikBaru: String,
        namaBaru: String,
        tanggalLahirBaru: String,
        jenisKelaminBaru: String,
        pendidikanTerakhirBaru: String,
        noBpjsBaru: String,
        keteranganBaru: String,
        statusKeluargaBaru: String,
        statusSipilBaru: String,
        statusWargaBaru: String,
        usiaBaru: String,
        kategoriUsiaBaru: String
    ) {
        val anggotaUpdate = anggotaLokal.copy(
            nik = nikBaru,
            nama = namaBaru,
            tanggalLahir = tanggalLahirBaru,
            jenisKelamin = jenisKelaminBaru,
            pendidikanTerakhir = pendidikanTerakhirBaru,
            noBpjs = noBpjsBaru,
            keterangan = keteranganBaru,
            statusKeluarga = statusKeluargaBaru,
            statusSipil = statusSipilBaru,
            statusWarga = statusWargaBaru,
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
                    noBpjs = noBpjsBaru,
                    keterangan = keteranganBaru,
                    statusKeluarga = statusKeluargaBaru,
                    statusSipil = statusSipilBaru
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
}