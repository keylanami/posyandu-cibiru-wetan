package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.repository.PilotRepository
import kotlinx.coroutines.launch

class PilotViewmodel(private val repository: PilotRepository) : ViewModel() {

    fun submitPhbs(
        token: String, patuhProtokolKesehatan: Int?, rumahJambanSehat: Int?, rumahAirBersih: Int?,
        kasusDiare: Int?, keluargaSadarGizi: Int?, rumahTanpaAsapRokok: Int?, babs: Int?
    ) {
        viewModelScope.launch {
            repository.updateDataPhbs(token, patuhProtokolKesehatan, rumahJambanSehat, rumahAirBersih, kasusDiare, keluargaSadarGizi, rumahTanpaAsapRokok, babs)
        }
    }

    fun submitPeduliStunting(
        token: String, bayiLahirPrematur: Int?, bayiBblr: Int?, balitaStunting: Int?,
        balitaRutinPemeriksaanTumbuhKembang: Int?, kehamilanTidakDirencankan: Int?, jarakKehamilanTerlaluDekat: Int?
    ) {
        viewModelScope.launch {
            repository.updateDataPeduliStunting(token, bayiLahirPrematur, bayiBblr, balitaStunting, balitaRutinPemeriksaanTumbuhKembang, kehamilanTidakDirencankan, jarakKehamilanTerlaluDekat)
        }
    }

    fun submitKia(
        token: String, ibuHamilRutinPeriksa: Int?, persalinanTenagaKesehatan: Int?, kematianIbuNifas: Int?,
        kankerServiks: Int?, imunisasiBayiBalita: Int?, batiBalitaSakitTerdata: Int?, kematianBayiBalita: Int?
    ) {
        viewModelScope.launch {
            repository.updateDataKia(token, ibuHamilRutinPeriksa, persalinanTenagaKesehatan, kematianIbuNifas, kankerServiks, imunisasiBayiBalita, batiBalitaSakitTerdata, kematianBayiBalita)
        }
    }

    fun submitSiagaKebakaran(
        token: String, kebakaranRumahTangga: Int?, kebakaranNonRumahTangga: Int?, rumahPunyaAparAtauAir: Int?,
        rumahSemiPermanenKayu: Int?, rumahPunyaP3k: Int?, kecelakaanRumahTangga: Int?, instalasiHydrant: Int?
    ) {
        viewModelScope.launch {
            repository.updateDataSiagaKebakaran(token, kebakaranRumahTangga, kebakaranNonRumahTangga, rumahPunyaAparAtauAir, rumahSemiPermanenKayu, rumahPunyaP3k, kecelakaanRumahTangga, instalasiHydrant)
        }
    }
}