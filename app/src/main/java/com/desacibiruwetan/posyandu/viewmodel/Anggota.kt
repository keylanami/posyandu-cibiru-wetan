package com.desacibiruwetan.posyandu.viewmodel

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity
import com.desacibiruwetan.posyandu.data.model.BalitaData
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.KbData
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

data class WargaProgramSummary(
    val balita: BalitaEntity? = null,
    val bumilLocal: BumilEntity? = null,
    val bumilRemote: List<BumilData> = emptyList(),
    val wusPusLocal: WusPusEntity? = null,
    val wusPusRemote: WusPusData? = null,
    val kbs: List<KbData> = emptyList()
) {
    val hasData: Boolean
        get() = balita != null || bumilLocal != null || bumilRemote.isNotEmpty() || wusPusLocal != null || wusPusRemote != null || kbs.isNotEmpty()
}


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

    private val _programSummaryState = MutableStateFlow<UiState<WargaProgramSummary>>(UiState.Idle)
    val programSummaryState = _programSummaryState.asStateFlow()


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
        tempatLahir: String? = null,
        golonganDarah: String? = null,
        suku: String? = null,
        kewarganegaraan: String = "WNI",
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
                kategoriUsia = kategoriUsia,
                tempatLahir = tempatLahir,
                golonganDarah = golonganDarah,
                suku = suku,
                kewarganegaraan = kewarganegaraan
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
        kategoriUsiaBaru: String,
        tempatLahirBaru: String? = null,
        golonganDarahBaru: String? = null,
        sukuBaru: String? = null,
        kewarganegaraanBaru: String? = null
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
                tempatLahirBaru = tempatLahirBaru,
                golonganDarahBaru = golonganDarahBaru,
                sukuBaru = sukuBaru,
                kewarganegaraanBaru = kewarganegaraanBaru,
            )
        }
    }

    fun updateDataBalita(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        tb: Double,
        bb: Double
    ) {
        viewModelScope.launch {
            repository.updateDataBalita(
                token,
                anggotaLocalId,
                anggotaServerId,
                tb,
                bb
            )
        }
    }

    fun createDataBalita(
        token: String,
        anggotaLocalId: Int,
        anggotaServerId: Int?,
        tb: Double,
        bb: Double
    ) {
        viewModelScope.launch {
            repository.addDataBalita(
                token,
                anggotaLocalId,
                anggotaServerId,
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

    fun getDetailBumilByAnggotaFromServer(token: String, anggotaId: Int) {
        viewModelScope.launch {
            _detailBumilState.value = UiState.Loading
            try {
                val response = repository.getBumilsByAnggotaId(token, anggotaId)
                val bumil = response.body()?.data?.firstOrNull()
                if (response.isSuccessful && bumil != null) {
                    _detailBumilState.value = UiState.Success(
                        BaseResponse(
                            success = true,
                            message = response.body()?.message ?: "Success",
                            data = bumil
                        )
                    )
                } else {
                    _detailBumilState.value = UiState.Error("Data Bumil belum tersedia")
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

    fun loadProgramSummary(token: String, anggotaLocalId: Int, anggotaServerId: Int?) {
        viewModelScope.launch {
            _programSummaryState.value = UiState.Loading
            val balita = repository.getBalitaLocalByAnggotaId(anggotaLocalId, anggotaServerId)
            val bumilLocal = repository.getBumilLocalByAnggotaId(anggotaLocalId, anggotaServerId)
            val wusPusLocal = repository.getWusPusLocalByAnggotaId(anggotaLocalId, anggotaServerId)

            var bumilRemote = emptyList<BumilData>()
            var wusPusRemote: WusPusData? = null
            if (anggotaServerId != null && token.isNotBlank()) {
                runCatching {
                    repository.getBumilsByAnggotaId(token, anggotaServerId)
                }.getOrNull()?.takeIf { it.isSuccessful }?.body()?.data?.let {
                    bumilRemote = it
                }

                runCatching {
                    repository.getDataWusPusById(token, anggotaServerId)
                }.getOrNull()?.takeIf { it.isSuccessful }?.body()?.data?.let {
                    wusPusRemote = it
                }
            }

            _programSummaryState.value = UiState.Success(
                WargaProgramSummary(
                    balita = balita,
                    bumilLocal = bumilLocal,
                    bumilRemote = bumilRemote,
                    wusPusLocal = wusPusLocal,
                    wusPusRemote = wusPusRemote,
                    kbs = wusPusRemote?.kbs.orEmpty()
                )
            )
        }
    }

    fun createKb(
        token: String,
        wusPusId: Int,
        jenisKb: String,
        tanggalMulaiKb: String?,
        statusAktif: Boolean,
        keterangan: String?,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            try {
                val response = repository.createKb(
                    token = token,
                    wusPusId = wusPusId,
                    jenisKb = jenisKb,
                    tanggalMulaiKb = tanggalMulaiKb,
                    statusAktif = statusAktif,
                    keterangan = keterangan
                )
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "Data KB berhasil disimpan")
                } else {
                    onResult(false, "Gagal menyimpan data KB: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(false, "Gagal menyimpan data KB: ${e.localizedMessage}")
            }
        }
    }

}
