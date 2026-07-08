package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KbRequest(
    @param:Json(name = "jenis_kb")
    val jenisKb: String,
    @param:Json(name = "tanggal_mulai_kb")
    val tanggalMulaiKb: String? = null,
    @param:Json(name = "status_aktif")
    val statusAktif: Boolean,
    val keterangan: String? = null
)

@JsonClass(generateAdapter = true)
data class KiaRequest(
    @param:Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int? = null,
    @param:Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int? = null,
    @param:Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int? = null,
    @param:Json(name = "kanker_serviks")
    val kankerServiks: Int? = null,
    @param:Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int? = null,
    @param:Json(name = "bayi_balita_sakit_terdata")
    val bayiBalitaSakitTerdata: Int? = null,
    @param:Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int? = null
)

@JsonClass(generateAdapter = true)
data class PhbsRequest(
    @param:Json(name = "patuh_protokol_kesehatan")
    val patuhProtokolKesehatan: Int? = null,
    @param:Json(name = "rumah_jamban_sehat")
    val rumahJambanSehat: Int? = null,
    @param:Json(name = "rumah_air_bersih")
    val rumahAirBersih: Int? = null,
    @param:Json(name = "kasus_diare")
    val kasusDiare: Int? = null,
    @param:Json(name = "keluarga_sadar_gizi")
    val keluargaSadarGizi: Int? = null,
    @param:Json(name = "rumah_tanpa_asap_rokok")
    val rumahTanpaAsapRokok: Int? = null,
    val babs: Int? = null
)

@JsonClass(generateAdapter = true)
data class PeduliStuntingRequest(
    @param:Json(name = "bayi_lahir_prematur")
    val bayiLahirPrematur: Int? = null,
    @param:Json(name = "bayi_bblr")
    val bayiBblr: Int? = null,
    @param:Json(name = "balita_kurang_gizi")
    val balitaKurangGizi: Int? = null,
    @param:Json(name = "balita_stunting")
    val balitaStunting: Int? = null,
    @param:Json(name = "balita_rutin_pemeriksaan_tumbuh_kembang")
    val balitaRutinPemeriksaanTumbuhKembang: Int? = null,
    @param:Json(name = "kehamilan_tidak_direncanakan")
    val kehamilanTidakDirencanakan: Int? = null,
    @param:Json(name = "jarak_kehamilan_terlalu_dekat")
    val jarakKehamilanTerlaluDekat: Int? = null
)

@JsonClass(generateAdapter = true)
data class SiagaKebakaranRequest(
    @param:Json(name = "kebakaran_rumah_tangga")
    val kebakaranRumahTangga: Int? = null,
    @param:Json(name = "kebakaran_non_rumah_tangga")
    val kebakaranNonRumahTangga: Int? = null,
    @param:Json(name = "rumah_punya_apar_atau_air")
    val rumahPunyaAparAtauAir: Int? = null,
    @param:Json(name = "rumah_semi_permanen_kayu")
    val rumahSemiPermanenKayu: Int? = null,
    @param:Json(name = "rumah_punya_p3k")
    val rumahPunyaP3k: Int? = null,
    @param:Json(name = "kecelakaan_rumah_tangga")
    val kecelakaanRumahTangga: Int? = null,
    @param:Json(name = "instalasi_hydrant")
    val instalasiHydrant: Int? = null
)

@JsonClass(generateAdapter = true)
data class ActivityLogData(
    val id: Int,
    @param:Json(name = "log_name")
    val logName: String,
    val description: String,
    @param:Json(name = "subject_type")
    val subjectType: String? = null,
    val event: String? = null,
    @param:Json(name = "subject_id")
    val subjectId: Int? = null,
    @param:Json(name = "causer_type")
    val causerType: String? = null,
    @param:Json(name = "causer_id")
    val causerId: Int? = null,
    @param:Json(name = "batch_uuid")
    val batchUuid: String? = null,
    @param:Json(name = "created_at")
    val createdAt: String? = null,
    @param:Json(name = "updated_at")
    val updatedAt: String? = null
)
