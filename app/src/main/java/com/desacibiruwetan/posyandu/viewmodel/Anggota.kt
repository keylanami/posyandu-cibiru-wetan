package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AnggotaViewmodel(private val repository: AnggotaRepository): ViewModel() {

    val listAnggotaLocal: StateFlow<List<AnggotaEntity>> = repository.getAllAnggotaLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  emptyList()
    )

    fun syncDataAnggotaDariServer(token: String) {
         viewModelScope.launch { repository.pullDataFromServer(token) }
    }


    fun getAnggotaKeluarga(keluargaId: Int): Flow<List<AnggotaEntity>> {
        return repository.getDetailAnggotaPerKeluarga(keluargaId)
    }


    fun tambahAnggota(
        token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String,
        jenisKelamin: String, pendidikanTerakhir: String, pekerjaan: String, noBpjs: String,
        statusKeluarga: String, statusSipil: String, statusWarga: String, keterangan: String,
        usia: String, kategoriUsia: String,
        onSuccess: (Int?) -> Unit = {}
    ){
        viewModelScope.launch {
            val serverId = repository.addNewAnggota(
                token = token,
                keluargaId = keluargaId,
                nik = nik,
                nama = nama,
                tanggalLahir = tanggalLahir,
                jenisKelamin = jenisKelamin,
                pendidikanTerakhir = pendidikanTerakhir,
                pekerjaan = pekerjaan,
                noBpjs = noBpjs,
                keterangan = keterangan,
                statusKeluarga = statusKeluarga,
                statusSipil = statusSipil,
                statusWarga = statusWarga,
                usia = usia,
                kategoriUsia = kategoriUsia
            )
            onSuccess(serverId)
        }
    }

    fun updateAnggota(
        token: String, anggotaLokal: AnggotaEntity, nikBaru: String, namaBaru: String, tanggalLahirBaru: String, jenisKelaminBaru: String, pendidikanTerakhirBaru: String, pekerjaanBaru: String, noBpjsBaru: String, keteranganBaru: String, statusKeluargaBaru: String, statusSipilBaru: String, statusWargaBaru: String, usiaBaru: String, kategoriUsiaBaru: String
    ) {
        viewModelScope.launch {
            repository.updateAnggota(
                token = token,
                anggotaLokal = anggotaLokal,
                nikBaru = nikBaru,
                namaBaru = namaBaru,
                tanggalLahirBaru = tanggalLahirBaru,
                jenisKelaminBaru = jenisKelaminBaru,
                pendidikanTerakhirBaru = pendidikanTerakhirBaru,
                pekerjaanBaru = pekerjaanBaru,
                noBpjsBaru = noBpjsBaru,
                keteranganBaru = keteranganBaru,
                statusKeluargaBaru = statusKeluargaBaru,
                statusSipilBaru = statusSipilBaru,
                statusWargaBaru = statusWargaBaru,
                usiaBaru = usiaBaru,
                kategoriUsiaBaru = kategoriUsiaBaru
            )
        }
    }

    fun updateDataBalita(token: String, anggotaServerId: Int, namaAyah: String, namaIbu: String, tb: Double, bb: Double) {
        viewModelScope.launch {
            repository.updateDataBalita(token, anggotaServerId, namaAyah, namaIbu, tb, bb)
        }
    }
}