package com.desacibiruwetan.posyandu.viewmodel

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.model.BalitaData
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.WusPusData
import com.desacibiruwetan.posyandu.data.network.BaseResponse
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AnggotaViewmodel(private val repository: AnggotaRepository) : ViewModel() {

    val listAnggotaLocal: StateFlow<List<AnggotaEntity>> = repository.getAllAnggotaLocal().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _detailBalitaState =
        MutableStateFlow<UiState<BaseResponse<BalitaData>>>(UiState.Idle)
    val detailBalitaState = _detailBalitaState.asStateFlow()


    private val _detailBumilState = MutableStateFlow<UiState<BaseResponse<BumilData>>>(UiState.Idle)
    val detailBumilState = _detailBumilState.asStateFlow()


    private val _detailWusPusState = MutableStateFlow<UiState<BaseResponse<WusPusData>>>(UiState.Idle)
    val detailWusPusState = _detailWusPusState.asStateFlow()


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
        onSuccess: (Int, Int?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            val (localId, serverId) = repository.addNewAnggota(
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
            onSuccess(localId, serverId)
        }
    }

    fun updateAnggota(
        token: String,
        anggotaLokal: AnggotaEntity,
        nikBaru: String,
        namaBaru: String,
        tanggalLahirBaru: String,
        jenisKelaminBaru: String,
        pendidikanTerakhirBaru: String,
        pekerjaanBaru: String,
        noBpjsBaru: String,
        keteranganBaru: String,
        statusKeluargaBaru: String,
        statusSipilBaru: String,
        statusWargaBaru: String,
        usiaBaru: String,
        kategoriUsiaBaru: String
    ) {
        viewModelScope.launch {
            repository.updateAnggota(
                token = token,
                anggotaLokal = anggotaLokal,
                nikBaru = nikBaru,
                namaBaru = namaBaru,
                tanggalLahirBaru = tanggalLahirBaru,
                jenisKelaminBaru = jenisKelaminBaru,
                pekerjaanBaru = pekerjaanBaru,
                pendidikanTerakhirBaru = pendidikanTerakhirBaru,
                noBpjsBaru = noBpjsBaru,
                keteranganBaru = keteranganBaru,
                statusKeluargaBaru = statusKeluargaBaru,
                statusSipilBaru = statusSipilBaru,
                statusWarga = statusWargaBaru,
                usiaBaru = usiaBaru,
                kategoriUsiaBaru = kategoriUsiaBaru,
            )
        }
    }

    fun updateDataBalita(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        namaAyah: String,
        namaIbu: String,
        tb: Double,
        bb: Double
    ) {
        viewModelScope.launch {
            repository.updateDataBalita(
                token,
                anggotaLocalId,
                anggotaServerId,
                namaAyah,
                namaIbu,
                tb,
                bb
            )
        }
    }

    fun createDataBalita(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        namaAyah: String,
        namaIbu: String,
        tb: Double,
        bb: Double
    ) {
        viewModelScope.launch {
            repository.addDataBalita(
                token,
                anggotaLocalId,
                anggotaServerId,
                namaAyah,
                namaIbu,
                tb,
                bb
            )
        }
    }

    fun getDetailBalitaFromServer(token: String, anggotaId: Int) {
        viewModelScope.launch {
            _detailBalitaState.value = UiState.Loading
            try {
                val response = repository.getBalitaById(token, anggotaId)
                if (response.isSuccessful && response.body() != null) {
                    _detailBalitaState.value = UiState.Success(response.body()!!)
                } else {
                    _detailBalitaState.value =
                        UiState.Error("Gagal memuat detail balita: ${response.message()}")
                }
            } catch (e: Exception) {
                _detailBalitaState.value = UiState.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }


    fun addDataBumil(
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
        viewModelScope.launch {
            repository.addDataBumil(
                token,
                anggotaLocalId,
                anggotaServerId,
                hamilKe,
                asiEksklusif,
                tglMulaiAsi,
                tglSelesaiAsi,
                createdAt,
                updatedAt
            )
        }
    }

    fun updateDataBumil(
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
        viewModelScope.launch {
            repository.updateDataBumil(
                token,
                anggotaLocalId,
                anggotaServerId,
                hamilKe,
                asiEksklusif,
                tglMulaiAsi,
                tglSelesaiAsi,
                createdAt,
                updatedAt
            )
        }
    }

    fun getDetailBumilFromServer(token: String, bumilId: Int) {
        viewModelScope.launch {
            _detailBumilState.value = UiState.Loading
            try {
                val response = repository.getBumilById(token, bumilId)
                if (response.isSuccessful && response.body() != null) {
                    _detailBumilState.value = UiState.Success(response.body()!!)
                } else {
                    _detailBumilState.value =
                        UiState.Error("Gagal memuat detail Bumil: ${response.message()}")
                }
            } catch (e: Exception) {
                _detailBumilState.value = UiState.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }



    fun updateDataWusPus(
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
        viewModelScope.launch {
            repository.updateDataWusPus(
                token, anggotaLocalId, anggotaServerId,
                namaSuami, statusKategori, tanggalMulaiStatus, keterangan,
                createdAt, updatedAt
            )
        }
    }

    fun deleteWuspusById(token: String, wuspusId: Int){
        viewModelScope.launch {
            repository.deleteDataWusPusById(token, wuspusId)
        }
    }




    fun getDetailWusPusFromServer(token: String, wuspusId: Int){
        viewModelScope.launch {
            _detailWusPusState.value = UiState.Loading

            try {
                val response = repository.getDataWusPusById(token, wuspusId)
                if (response.isSuccessful && response.body() != null){
                    _detailWusPusState.value = UiState.Success(response.body()!!)
                } else {
                    _detailWusPusState.value = UiState.Error("Gagal memuat detail WUS/PUS: ${response.message()}")
                }

            } catch (e: Exception){
                _detailWusPusState.value = UiState.Error("Terjadi kesalahan ${e.localizedMessage}")
            }
        }
    }


    fun updateDataKb(
        token: String, kbLocalId: Int, kbServerId: Int?, wusPusIdServer: Int,
        jenisKb: String, tanggalMulaiKb: String?, statusAktif: Boolean, keterangan: String?,
        createdAt: String, updatedAt: String
    ){
        viewModelScope.launch {
            repository.updateDataKb(
                token, kbLocalId, kbServerId, wusPusIdServer,
                jenisKb, tanggalMulaiKb, statusAktif, keterangan, createdAt, updatedAt
            )
        }
    }

}