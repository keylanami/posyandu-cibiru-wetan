package com.desacibiruwetan.posyandu.viewmodel

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.network.ApiService
import com.desacibiruwetan.posyandu.data.network.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class ReadRecord(
    val id: String,
    val title: String,
    val subtitle: String,
    val meta: String,
    val details: List<Pair<String, String>> = emptyList()
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
    val previewLimit: Int = 5,
    val mapper: suspend (JSONObject) -> ReadRecord
)

class DataReadViewModel(
    private val apiService: ApiService,
    private val anggotaDao: AnggotaDao,
    context: Context
) : ViewModel() {
    private val appContext = context.applicationContext
    private val _readState = MutableStateFlow<UiState<List<ReadCollection>>>(UiState.Idle)
    val readState: StateFlow<UiState<List<ReadCollection>>> = _readState.asStateFlow()

    init {
        loadCache()?.let { _readState.value = UiState.Success(it) }
    }

    fun refresh(token: String, onFinished: () -> Unit = {}) {
        if (token.isBlank()) {
            onFinished()
            return
        }
        viewModelScope.launch {
            try {
                if (_readState.value !is UiState.Success) {
                    _readState.value = UiState.Loading
                }
                val collections = readEndpoints.map { endpoint ->
                    fetchCollection(token, endpoint)
                }
                saveCache(collections)
                _readState.value = UiState.Success(collections)
            } finally {
                onFinished()
            }
        }
    }

    fun clearCache() {
        appContext.getSharedPreferences("data_read_cache", Context.MODE_PRIVATE).edit {
            clear()
        }
        _readState.value = UiState.Idle
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

            val records = (0 until minOf(items.length(), endpoint.previewLimit)).mapNotNull { index ->
                items.optJSONObject(index)?.let { endpoint.mapper(it) }
            }

                ReadCollection(
                    key = endpoint.key,
                    title = endpoint.title,
                    endpoint = endpoint.title,
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
            endpoint = title,
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
                            put("details", JSONArray().apply {
                                record.details.forEach { (label, value) ->
                                    put(JSONObject().apply {
                                        put("label", label)
                                        put("value", value)
                                    })
                                }
                            })
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
                            val detailsArray = record.optJSONArray("details") ?: JSONArray()
                            ReadRecord(
                                id = record.optString("id"),
                                title = record.optString("title"),
                                subtitle = record.optString("subtitle"),
                                meta = record.optString("meta"),
                                details = (0 until detailsArray.length()).mapNotNull { detailIndex ->
                                    detailsArray.optJSONObject(detailIndex)?.let { detail ->
                                        detail.optString("label") to detail.optString("value")
                                    }
                                }
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

    private fun JSONObject.objectOrNull(vararg keys: String): JSONObject? {
        keys.forEach { key ->
            optJSONObject(key)?.let { return it }
        }
        return null
    }

    private fun JSONObject.boolText(key: String, yes: String = "Ya", no: String = "Tidak"): String {
        return when {
            !has(key) || isNull(key) -> "-"
            optBoolean(key) -> yes
            else -> no
        }
    }

    private fun JSONObject.valueText(key: String): String {
        val raw = opt(key) ?: return "-"
        if (raw == JSONObject.NULL) return "-"
        if (raw is Boolean) return if (raw) "Ya" else "Tidak"
        return formatIndonesianDate(raw.toString(), dateOnly = key.startsWith("tanggal_"))
            .takeIf { it != raw.toString() }
            ?: raw.toString()
    }

    private fun JSONObject.compactRows(vararg keys: Pair<String, String>, limit: Int = 6): List<Pair<String, String>> {
        return keys.mapNotNull { (key, label) ->
            val value = valueText(key)
            if (value.isBlank() || value == "-") null else label to value
        }.take(limit)
    }

    private fun readableModule(value: String): String {
        return when (value) {
            "anggotas" -> "Warga"
            "rumahs" -> "Rumah"
            "keluargas" -> "Keluarga"
            "balitas" -> "Balita"
            "bumils" -> "Bumil"
            "wus_pus" -> "WUS/PUS"
            "kbs" -> "KB"
            "auth" -> "Akun"
            else -> value.replace("_", " ").replace("-", " ").replaceFirstChar { it.titlecase(Locale("id", "ID")) }
        }
    }

    private fun readableEvent(value: String): String {
        return when (value.lowercase(Locale.US)) {
            "created" -> "Menambah"
            "updated" -> "Mengubah"
            "deleted" -> "Menghapus"
            "login" -> "Masuk akun"
            "logout" -> "Keluar akun"
            else -> value.replace("_", " ").replaceFirstChar { it.titlecase(Locale("id", "ID")) }
        }
    }

    private fun logSummary(logName: String, event: String, data: JSONObject): String {
        val source = data.objectOrNull("subject") ?: data.objectOrNull("properties")?.objectOrNull("attributes") ?: data
        return when (logName) {
            "anggotas" -> source.text("nama", fallback = "Data warga diperbarui")
            "rumahs" -> "Rumah ${source.text("no_rumah", fallback = "-")} - ${source.text("alamat", fallback = "alamat belum tersedia")}"
            "keluargas" -> "KK ${source.text("no_kk", fallback = "-")}"
            "balitas" -> source.text("nama", fallback = "Data balita diperbarui")
            "bumils" -> "Hamil ke-${source.text("hamil_ke", fallback = "-")} - ASI eksklusif ${source.boolText("asi_eksklusif")}"
            "wus_pus" -> "${source.text("status_kategori", fallback = "WUS/PUS")} - ${source.text("nama_suami", fallback = "tanpa nama suami")}"
            "kbs" -> "${source.text("jenis_kb", fallback = "KB")} - ${source.boolText("status_aktif", yes = "aktif", no = "tidak aktif")}"
            "auth" -> if (event == "login") "Login berhasil" else data.text("description", fallback = "Aktivitas akun")
            else -> data.text("description", fallback = "Aktivitas ${readableModule(logName)}")
        }
    }

    private fun logRows(logName: String, event: String, data: JSONObject): List<Pair<String, String>> {
        val source = data.objectOrNull("subject") ?: data.objectOrNull("properties")?.objectOrNull("attributes") ?: data
        val coreRows = listOf(
            "Modul" to readableModule(logName),
            "Aksi" to readableEvent(event),
            "Waktu" to formatIndonesianDate(data.text("created_at", fallback = "-"))
        )
        val subjectRows = when (logName) {
            "anggotas" -> source.compactRows(
                "nama" to "Nama",
                "nik" to "NIK",
                "tanggal_lahir" to "Tanggal lahir",
                "jenis_kelamin" to "Jenis kelamin",
                "status_keluarga" to "Status keluarga",
                "status_sipil" to "Status sipil",
                "pekerjaan" to "Pekerjaan"
            )
            "rumahs" -> source.compactRows(
                "no_rumah" to "No rumah",
                "alamat" to "Alamat"
            )
            "keluargas" -> source.compactRows(
                "no_kk" to "No KK",
                "isNgontrak" to "Ngontrak",
                "isGakin" to "Gakin"
            )
            "bumils" -> source.compactRows(
                "hamil_ke" to "Hamil ke",
                "asi_eksklusif" to "ASI eksklusif",
                "tanggal_mulai_asi" to "Mulai ASI",
                "tanggal_selesai_asi" to "Selesai ASI"
            )
            "wus_pus" -> source.compactRows(
                "status_kategori" to "Kategori",
                "nama_suami" to "Nama suami",
                "tanggal_mulai_status" to "Mulai status",
                "keterangan" to "Keterangan"
            )
            "kbs" -> source.compactRows(
                "jenis_kb" to "Jenis KB",
                "tanggal_mulai_kb" to "Mulai KB",
                "status_aktif" to "Status aktif",
                "keterangan" to "Keterangan"
            )
            else -> emptyList()
        }

        return (coreRows + subjectRows).distinctBy { it.first }
    }

    private fun formatIndonesianDate(value: String?, dateOnly: Boolean = false): String {
        val source = value?.trim().orEmpty()
        if (source.isBlank() || source == "null") return "-"

        val locale = Locale("id", "ID")
        val outputPattern = if (!dateOnly && source.contains(":")) "d MMMM yyyy, HH.mm" else "d MMMM yyyy"
        val output = SimpleDateFormat(outputPattern, locale)
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "d MMMM yyyy, HH.mm",
            "d MMMM yyyy"
        )

        patterns.forEach { pattern ->
            val parser = SimpleDateFormat(pattern, locale).apply {
                isLenient = false
                if (pattern.endsWith("'Z'")) {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
            }
            runCatching { parser.parse(source) }.getOrNull()?.let { return output.format(it) }
        }

        return source
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
            val anggotaId = it.optInt("anggota_id")
            val nama = it.text("nama").takeIf { n -> n != "-" }
                ?: anggotaDao.getAnggotaByLocalOrServerId(anggotaId)?.nama
                ?: "Anggota $anggotaId"

            ReadRecord(
                id = it.text("id"),
                title = nama,
                subtitle = "Hamil ke-${it.text("hamil_ke")}",
                meta = if (it.optBoolean("asi_eksklusif", false)) "ASI eksklusif" else "Belum ASI eksklusif"
            )
        },
        ReadEndpoint(
            key = "wuspus",
            title = "WUS/PUS",
            path = "wus-pus",
            description = "Wanita/pasangan usia subur and statusnya"
        ) {
            val anggotaId = it.optInt("anggota_id")
            val nama = it.text("nama").takeIf { n -> n != "-" }
                ?: anggotaDao.getAnggotaByLocalOrServerId(anggotaId)?.nama
                ?: "Anggota $anggotaId"

            ReadRecord(
                id = it.text("id"),
                title = nama,
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
            canCreate = false,
            previewLimit = 20
        ) {
            val logName = it.text("log_name", fallback = "log")
            val event = it.text("event", "description", fallback = "aktivitas")
            val module = readableModule(logName)
            val action = readableEvent(event)
            ReadRecord(
                id = it.text("id"),
                title = if (logName == "auth") action else "$action $module",
                subtitle = logSummary(logName, event, it),
                meta = formatIndonesianDate(it.text("created_at", fallback = "Waktu tidak tersedia")),
                details = logRows(logName, event, it)
            )
        }
    )
}
