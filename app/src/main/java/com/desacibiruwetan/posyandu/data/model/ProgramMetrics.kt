package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KbRequest(
    @Json(name = "jenis_kb")
    val jenisKb: String,
    @Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,
    @Json(name = "status_aktif")
    val statusAktif: Boolean,
    val keterangan: String? = null
)

@JsonClass(generateAdapter = true)
data class KiaRequest(
    @Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int? = null,
    @Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int? = null,
    @Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int? = null,
    @Json(name = "kanker_serviks")
    val kankerServiks: Int? = null,
    @Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int? = null,
    @Json(name = "bayi_balita_sakit_terdata")
    val bayiBalitaSakitTerdata: Int? = null,
    @Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int? = null
)

@JsonClass(generateAdapter = true)
data class PhbsRequest(
    @Json(name = "patuh_protokol_kesehatan")
    val patuhProtokolKesehatan: Int? = null,
    @Json(name = "rumah_jamban_sehat")
    val rumahJambanSehat: Int? = null,
    @Json(name = "rumah_air_bersih")
    val rumahAirBersih: Int? = null,
    @Json(name = "kasus_diare")
    val kasusDiare: Int? = null,
    @Json(name = "keluarga_sadar_gizi")
    val keluargaSadarGizi: Int? = null,
    @Json(name = "rumah_tanpa_asap_rokok")
    val rumahTanpaAsapRokok: Int? = null,
    val babs: Int? = null
)

@JsonClass(generateAdapter = true)
data class PeduliStuntingRequest(
    @Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int? = null,
    @Json(name = "bayi_bblr")
    val bayiBblr: Int? = null,
    @Json(name = "balita_kurang_gizi")
    val balitaKurangGizi: Int? = null,
    @Json(name = "balita_stunting")
    val balitaStunting: Int? = null,
    @Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int? = null,
    @Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencanakan: Int? = null,
    @Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int? = null
)

@JsonClass(generateAdapter = true)
data class SiagaKebakaranRequest(
    @Json(name = "kebakaran_rumah_tangga")
    val kebakaranRumahTangga: Int? = null,
    @Json(name = "kebakaran_non_rumah_tangga")
    val kebakaranNonRumahTangga: Int? = null,
    @Json(name = "rumah_punya_apar_atau_air")
    val rumahPunyaAparAtauAir: Int? = null,
    @Json(name = "rumah_semi_permanen_kayu")
    val rumahSemiPermanenKayu: Int? = null,
    @Json(name = "rumah_punya_p3k")
    val rumahPunyaP3k: Int? = null,
    @Json(name = "kecelakaan_rumah_tangga")
    val kecelakaanRumahTangga: Int? = null,
    @Json(name = "instalasi_hydrant")
    val instalasiHydrant: Int? = null
)

@JsonClass(generateAdapter = true)
data class ActivityLogData(
    val id: Int,
    @Json(name = "log_name")
    val logName: String,
    val description: String,
    @Json(name = "subject_type")
    val subjectType: String? = null,
    val event: String? = null,
    @Json(name = "subject_id")
    val subjectId: Int? = null,
    @Json(name = "causer_type")
    val causerType: String? = null,
    @Json(name = "causer_id")
    val causerId: Int? = null,
    @Json(name = "batch_uuid")
    val batchUuid: String? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null
)
