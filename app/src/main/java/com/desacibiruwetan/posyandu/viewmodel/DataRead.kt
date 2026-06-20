package com.desacibiruwetan.posyandu.viewmodel

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

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

private data class ReadEndpoint(
    val key: String,
    val title: String,
    val path: String,
    val description: String,
    val canCreate: Boolean = true,
    val mapper: (JSONObject) -> ReadRecord
)

class DataReadViewModel(
    private val apiService: ApiService,
    context: Context
) : ViewModel() {
    private val appContext = context.applicationContext
    private val _readState = MutableStateFlow<UiState<List<ReadCollection>>>(UiState.Idle)
    val readState: StateFlow<UiState<List<ReadCollection>>> = _readState.asStateFlow()

    init {
        loadCache()?.let { _readState.value = UiState.Success(it) }
    }

    fun refresh(token: String) {
        if (token.isBlank()) return
        viewModelScope.launch {
            if (_readState.value !is UiState.Success) {
                _readState.value = UiState.Loading
            }
            val collections = readEndpoints.map { endpoint ->
                fetchCollection(token, endpoint)
            }
            saveCache(collections)
            _readState.value = UiState.Success(collections)
        }
    }

    private suspend fun fetchCollection(token: String, endpoint: ReadEndpoint): ReadCollection {
        return try {
            val response = apiService.getRawReadCollection(endpoint.path, token)
            if (!response.isSuccessful) {
                return endpoint.errorCollection("HTTP ${response.code()} ${response.message()}")
            }

            val raw = response.body()?.string().orEmpty()
            if (raw.isBlank()) return endpoint.errorCollection("Respons kosong")

            val root = JSONObject(raw)
            val payload = root.opt("data")
            val items = payload.extractArray()
            val count = when (payload) {
                is JSONObject -> payload.optInt("total", items.length())
                is JSONArray -> items.length()
                else -> items.length()
            }

            val records = (0 until minOf(items.length(), 5)).mapNotNull { index ->
                items.optJSONObject(index)?.let(endpoint.mapper)
            }

            ReadCollection(
                key = endpoint.key,
                title = endpoint.title,
                endpoint = "GET /${endpoint.path}",
                description = endpoint.description,
                count = count,
                records = records,
                canCreate = endpoint.canCreate
            )
        } catch (e: Exception) {
            endpoint.errorCollection(e.localizedMessage ?: "Gagal membaca data")
        }
    }

    private fun Any?.extractArray(): JSONArray {
        return when (this) {
            is JSONArray -> this
            is JSONObject -> {
                when {
                    opt("data") is JSONArray -> getJSONArray("data")
                    opt("items") is JSONArray -> getJSONArray("items")
                    opt("records") is JSONArray -> getJSONArray("records")
                    else -> JSONArray().put(this)
                }
            }
            else -> JSONArray()
        }
    }

    private fun ReadEndpoint.errorCollection(message: String): ReadCollection {
        return ReadCollection(
            key = key,
            title = title,
            endpoint = "GET /$path",
            description = description,
            count = 0,
            records = listOf(
                ReadRecord(
                    id = "error",
                    title = "Gagal memuat",
                    subtitle = message,
                    meta = "Coba sinkronisasi ulang saat jaringan tersedia"
                )
            ),
            canCreate = canCreate
        )
    }

    private fun saveCache(collections: List<ReadCollection>) {
        val array = JSONArray()
        collections.forEach { collection ->
            array.put(JSONObject().apply {
                put("key", collection.key)
                put("title", collection.title)
                put("endpoint", collection.endpoint)
                put("description", collection.description)
                put("count", collection.count)
                put("canCreate", collection.canCreate)
                put("records", JSONArray().apply {
                    collection.records.forEach { record ->
                        put(JSONObject().apply {
                            put("id", record.id)
                            put("title", record.title)
                            put("subtitle", record.subtitle)
                            put("meta", record.meta)
                        })
                    }
                })
            })
        }
        appContext.getSharedPreferences("data_read_cache", Context.MODE_PRIVATE).edit {
            putString("collections", array.toString())
        }
    }

    private fun loadCache(): List<ReadCollection>? {
        val raw = appContext
            .getSharedPreferences("data_read_cache", Context.MODE_PRIVATE)
            .getString("collections", null)
            ?: return null

        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).mapNotNull { index ->
                val collection = array.optJSONObject(index) ?: return@mapNotNull null
                val recordsArray = collection.optJSONArray("records") ?: JSONArray()
                ReadCollection(
                    key = collection.optString("key"),
                    title = collection.optString("title"),
                    endpoint = collection.optString("endpoint"),
                    description = collection.optString("description"),
                    count = collection.optInt("count"),
                    canCreate = collection.optBoolean("canCreate", true),
                    records = (0 until recordsArray.length()).mapNotNull { recordIndex ->
                        recordsArray.optJSONObject(recordIndex)?.let { record ->
                            ReadRecord(
                                id = record.optString("id"),
                                title = record.optString("title"),
                                subtitle = record.optString("subtitle"),
                                meta = record.optString("meta")
                            )
                        }
                    }
                )
            }
        }.getOrNull()
    }

    private fun JSONObject.text(vararg keys: String, fallback: String = "-"): String {
        keys.forEach { key ->
            val value = optString(key, "")
            if (value.isNotBlank() && value != "null") return value
        }
        return fallback
    }

    private val readEndpoints = listOf(
        ReadEndpoint(
            key = "balita",
            title = "Balita",
            path = "balitas",
            description = "Anak balita yang sudah punya data pertumbuhan"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = it.text("nama", fallback = "Balita #${it.text("id")}"),
                subtitle = it.text("kategori_usia", fallback = "Data balita"),
                meta = "Keluarga ${it.text("keluarga_id")}"
            )
        },
        ReadEndpoint(
            key = "bumil",
            title = "Bumil",
            path = "bumils",
            description = "Ibu hamil dan data ASI/kehamilan"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "Anggota ${it.text("anggota_id")}",
                subtitle = "Hamil ke-${it.text("hamil_ke")}",
                meta = if (it.optBoolean("asi_eksklusif", false)) "ASI eksklusif" else "Belum ASI eksklusif"
            )
        },
        ReadEndpoint(
            key = "wuspus",
            title = "WUS/PUS",
            path = "wus-pus",
            description = "Wanita/pasangan usia subur dan statusnya"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "Anggota ${it.text("anggota_id")}",
                subtitle = it.text("status_kategori", fallback = "Status belum diisi"),
                meta = it.text("nama_suami", fallback = "Tanpa nama suami")
            )
        },
        ReadEndpoint(
            key = "kb",
            title = "KB",
            path = "kbs",
            description = "Riwayat dan status penggunaan kontrasepsi"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = it.text("jenis_kb", fallback = "KB #${it.text("id")}"),
                subtitle = if (it.optBoolean("status_aktif", false)) "Aktif" else "Tidak aktif",
                meta = it.text("tanggal_mulai_kb", fallback = "Tanggal belum diisi")
            )
        },
        ReadEndpoint(
            key = "kia",
            title = "KIA",
            path = "kias",
            description = "Indikator kesehatan ibu dan anak"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "KIA #${it.text("id")}",
                subtitle = "Ibu periksa: ${it.optInt("ibu_hamil_rutin_periksa", 0)}",
                meta = "Imunisasi: ${it.optInt("imunisasi_bayi_balita", 0)}"
            )
        },
        ReadEndpoint(
            key = "phbs",
            title = "PHBS",
            path = "phbs",
            description = "Perilaku hidup bersih dan sehat"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "PHBS #${it.text("id")}",
                subtitle = "Air bersih: ${it.optInt("rumah_air_bersih", 0)}",
                meta = "Jamban sehat: ${it.optInt("rumah_jamban_sehat", 0)}"
            )
        },
        ReadEndpoint(
            key = "stunting",
            title = "Peduli Stunting",
            path = "peduli-stuntings",
            description = "Risiko dan pemantauan stunting"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "Stunting #${it.text("id")}",
                subtitle = "Balita stunting: ${it.optInt("balita_stunting", 0)}",
                meta = "Kurang gizi: ${it.optInt("balita_kurang_gizi", 0)}"
            )
        },
        ReadEndpoint(
            key = "kebakaran",
            title = "Siaga Kebakaran",
            path = "siaga-kebakarans",
            description = "Indikator kesiapsiagaan rumah"
        ) {
            ReadRecord(
                id = it.text("id"),
                title = "Kebakaran #${it.text("id")}",
                subtitle = "APAR/air: ${it.optInt("rumah_punya_apar_atau_air", 0)}",
                meta = "P3K: ${it.optInt("rumah_punya_p3k", 0)}"
            )
        },
        ReadEndpoint(
            key = "logs",
            title = "Log Aktivitas",
            path = "log-aktivitas",
            description = "Jejak aktivitas sistem dan kader",
            canCreate = false
        ) {
            ReadRecord(
                id = it.text("id"),
                title = it.text("description", fallback = "Aktivitas #${it.text("id")}"),
                subtitle = it.text("event", "log_name", fallback = "Log"),
                meta = it.text("created_at", fallback = "Waktu tidak tersedia")
            )
        }
    )
}
