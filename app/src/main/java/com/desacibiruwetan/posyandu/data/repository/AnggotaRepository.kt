package com.desacibiruwetan.posyandu.data.repository

import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.model.AnggotaReq
import com.desacibiruwetan.posyandu.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class AnggotaRepository(
    private val apiService: ApiService,
    private val anggotaDao: AnggotaDao
){

    fun getAllAnggotaLocal(): Flow<List<AnggotaEntity>> {
        return anggotaDao.getAllAnggotaDao()
    }

   fun getDetailAnggotaPerRumah(rumahId: Int): Flow<List<AnggotaEntity>>{
       return anggotaDao.getAnggotaByKeluargaId(rumahId)
   }

    suspend fun addNewAnggota(token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String, jenisKelamin: String, pendidikanTerakhir: String, noBpjs: String, keterangan: String){
        val entitasBaru = AnggotaEntity(
            keluargaId = keluargaId,
            nik = nik,
            noBpjs = noBpjs,
            nama = nama,
            tanggalLahir = tanggalLahir,
            jenisKelamin = jenisKelamin,
            pendidikanTerakhir = pendidikanTerakhir,
            keterangan = keterangan,
            isSynced = false
        )
        
        val localIdBaru = anggotaDao.insertAnggotaLocal(entitasBaru)
        
        try {
            val request = AnggotaReq(
                keluargaId = keluargaId,
                nik = nik,
                nama = nama,
                tanggalLahir = tanggalLahir,
                jenisKelamin = jenisKelamin,
                pendidikanTerakhir = pendidikanTerakhir,
                noBpjs = noBpjs,
            )

            val response = apiService.postAnggota(token, keluargaId, request)


            if (response.isSuccessful){
                val dataServer = response.body()?.data

                if (dataServer!=null){
                    val anggotaSukses = entitasBaru.copy(
                        localId = localIdBaru.toInt(),
                        serverId = dataServer.id,
                        isSynced = true
                    )

                    anggotaDao.insertAnggotaLocal(anggotaSukses)
                }
            }
        } catch (e: Exception){
            println("Sedang offline, data anggota disimpan di memori HP dulu")
        }
    }

    suspend fun updateAnggota(token: String, anggotaLokal: AnggotaEntity, nikBaru: String, namaBaru: String, tanggalLahirBaru: String, jenisKelaminBaru: String, pendidikanTerakhirBaru: String, noBpjsBaru: String, keteranganBaru: String){
        val anggotaUpdate = anggotaLokal.copy(
            nik = nikBaru,
            nama = namaBaru,
            tanggalLahir = tanggalLahirBaru,
            jenisKelamin = jenisKelaminBaru,
            pendidikanTerakhir = pendidikanTerakhirBaru,
            noBpjs = noBpjsBaru,
            keterangan = keteranganBaru,
            isSynced = false
        )

        anggotaDao.updateAnggotaLocal(anggotaUpdate)

        try {

            val request = AnggotaReq(
                nik = nikBaru,
                nama = namaBaru,
                tanggalLahir = tanggalLahirBaru,
                jenisKelamin = jenisKelaminBaru,
                pendidikanTerakhir = pendidikanTerakhirBaru,
                noBpjs = noBpjsBaru,
                keterangan = keteranganBaru,
                keluargaId = anggotaUpdate.keluargaId
            )

            val response = apiService.putAnggotaId(token, anggotaUpdate.keluargaId, request)

            if (response.isSuccessful){
                anggotaDao.updateAnggotaLocal(anggotaUpdate.copy(isSynced = true))
            }
        } catch (e: Exception){
            println("Sedang offline, data anggota disimpan di memori HP dulu")
        }
    }
}
