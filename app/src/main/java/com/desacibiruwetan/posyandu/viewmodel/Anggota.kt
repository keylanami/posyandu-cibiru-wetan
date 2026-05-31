package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
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

    fun syncDataAnggota(keluargaId: Int){
        viewModelScope.launch {
            repository.getDetailAnggotaPerRumah(keluargaId)
        }
    }

    fun tambahAnggota(token: String, keluargaId: Int, nik: String, nama: String, tanggalLahir: String, jenisKelamin: String, pendidikanTerakhir: String, noBpjs: String, keterangan: String){
        viewModelScope.launch {
            repository.addNewAnggota(token, keluargaId, nik, nama, tanggalLahir, jenisKelamin, pendidikanTerakhir, noBpjs, keterangan)
        }
    }

    fun updateKeluarga(token: String, anggotaLokal: AnggotaEntity, nikBaru: String, namaBaru: String, tanggalLahirBaru: String, jenisKelaminBaru: String, pendidikanTerakhirBaru: String, noBpjsBaru: String, keteranganBaru: String){
        viewModelScope.launch {
            repository.updateAnggota(token, anggotaLokal, nikBaru, namaBaru, tanggalLahirBaru, jenisKelaminBaru, pendidikanTerakhirBaru, noBpjsBaru, keteranganBaru)
        }
    }
}