package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KiaData(
    val id: Int,

    @Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int?= null,

    @Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int?= null,

    @Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int?= null,

    @Json(name = "kanker_serviks")
    val kankerServiks: Int?= null,

    @Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int?= null,

    @Json(name = "bati_balita_sakit_terdata")
    val batiBalitaSakitTerdata: Int?= null,

    @Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null
)



@JsonClass(generateAdapter = true)
data class KiaReq(

    @Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int?= null,

    @Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int?= null,

    @Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int?= null,

    @Json(name = "kanker_serviks")
    val kankerServiks: Int?= null,

    @Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int?= null,

    @Json(name = "bati_balita_sakit_terdata")
    val batiBalitaSakitTerdata: Int?= null,

    @Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int?= null,
)