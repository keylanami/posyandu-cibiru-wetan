package com.desacibiruwetan.posyandu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReadRecord(
    val id: String,
    val title: String,
    val subtitle: String,
    val meta: String
)

data class ReadCollection(
    val key: String,
    val title: String,
    val endpoint: String,
    val description: String,
    val count: Int,
    val records: List<ReadRecord>,
    val canCreate: Boolean = true
)

class DataReadViewModel(private val apiService: ApiService) : ViewModel() {
    private val _readState = MutableStateFlow<UiState<List<ReadCollection>>>(UiState.Idle)
    val readState: StateFlow<UiState<List<ReadCollection>>> = _readState.asStateFlow()

    fun refresh(token: String) {
        if (token.isBlank()) return
        viewModelScope.launch {
            _readState.value = UiState.Loading
            try {
                val collections = listOf(
                    async {
                        val data = apiService.getAllBalita(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "balita",
                            title = "Balita",
                            endpoint = "GET /balitas",
                            description = "Anak balita yang sudah punya data pertumbuhan",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), it.nama, it.kategoriUsia, "Keluarga ${it.keluargaId}")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllBumil(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "bumil",
                            title = "Bumil",
                            endpoint = "GET /bumils",
                            description = "Ibu hamil dan data ASI/kehamilan",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "Anggota ${it.anggotaId}", "Hamil ke-${it.hamilKe}", if (it.asiEksklusif) "ASI eksklusif" else "Belum ASI eksklusif")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllWusPus(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "wuspus",
                            title = "WUS/PUS",
                            endpoint = "GET /wus-pus",
                            description = "Wanita/pasangan usia subur dan statusnya",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "Anggota ${it.anggotaId}", it.statusKategori, it.namaSuami ?: "Tanpa nama suami")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllKb(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "kb",
                            title = "KB",
                            endpoint = "GET /kbs",
                            description = "Riwayat dan status penggunaan kontrasepsi",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), it.jenisKb, if (it.statusAktif) "Aktif" else "Tidak aktif", it.tanggalMulaiKb ?: "Tanggal belum diisi")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllKia(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "kia",
                            title = "KIA",
                            endpoint = "GET /kias",
                            description = "Indikator kesehatan ibu dan anak",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "KIA #${it.id}", "Ibu periksa: ${it.ibuHamilRutinPeriksa ?: 0}", "Imunisasi: ${it.imunisasiBayiBalita ?: 0}")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllPhbs(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "phbs",
                            title = "PHBS",
                            endpoint = "GET /phbs",
                            description = "Perilaku hidup bersih dan sehat",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "PHBS #${it.id}", "Air bersih: ${it.rumahAirBersih ?: 0}", "Jamban sehat: ${it.rumahJambanSehat ?: 0}")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllPeduliStunting(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "stunting",
                            title = "Peduli Stunting",
                            endpoint = "GET /peduli-stuntings",
                            description = "Risiko dan pemantauan stunting",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "Stunting #${it.id}", "Balita stunting: ${it.balitaStunting ?: 0}", "Kurang gizi: ${it.balitaKurangGizi ?: 0}")
                            }
                        )
                    },
                    async {
                        val data = apiService.getAllSiagaKebakaran(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "kebakaran",
                            title = "Siaga Kebakaran",
                            endpoint = "GET /siaga-kebakarans",
                            description = "Indikator kesiapsiagaan rumah",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), "Kebakaran #${it.id}", "APAR/air: ${it.rumahPunyaAparAtauAir ?: 0}", "P3K: ${it.rumahPunyaP3k ?: 0}")
                            }
                        )
                    },
                    async {
                        val data = apiService.getActivityLogs(token).body()?.data.orEmpty()
                        ReadCollection(
                            key = "logs",
                            title = "Log Aktivitas",
                            endpoint = "GET /log-aktivitas",
                            description = "Jejak aktivitas sistem dan kader",
                            count = data.size,
                            records = data.take(5).map {
                                ReadRecord(it.id.toString(), it.description, it.event ?: it.logName, it.createdAt ?: "Waktu tidak tersedia")
                            },
                            canCreate = false
                        )
                    }
                ).awaitAll()
                _readState.value = UiState.Success(collections)
            } catch (e: Exception) {
                _readState.value = UiState.Error(e.localizedMessage ?: "Gagal memuat data baca")
            }
        }
    }
}
