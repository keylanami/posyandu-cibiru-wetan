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


    fun tambahAnggota(token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String, jenisKelamin: String, pendidikanTerakhir: String, noBpjs: String, statusKeluarga: String, statusSipil: String, statusWarga: String, keterangan: String, usia: String, kategoriUsia: String){
        viewModelScope.launch {
            repository.addNewAnggota(token, keluargaId, nik, nama, tanggalLahir, jenisKelamin, pendidikanTerakhir, noBpjs, statusKeluarga, statusSipil, statusWarga, keterangan, usia, kategoriUsia)
        }
    }

    fun updateAnggota(token: String, anggotaLokal: AnggotaEntity, nikBaru: String, namaBaru: String, tanggalLahirBaru: String, jenisKelaminBaru: String, pendidikanTerakhirBaru: String, noBpjsBaru: String, keteranganBaru: String, statusKeluargaBaru: String, statusSipilBaru: String, statusWargaBaru: String, usiaBaru: String, kategoriUsiaBaru: String) {
        viewModelScope.launch {
            repository.updateAnggota(token, anggotaLokal, nikBaru, namaBaru, tanggalLahirBaru, jenisKelaminBaru, pendidikanTerakhirBaru, noBpjsBaru, keteranganBaru, statusKeluargaBaru, statusSipilBaru, statusWargaBaru, usiaBaru, kategoriUsiaBaru)
        }
    }
}