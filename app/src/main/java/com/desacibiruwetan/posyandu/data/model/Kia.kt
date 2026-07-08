package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class KiaData(
    val id: Int,

    @param:Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int?= null,

    @param:Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int?= null,

    @param:Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int?= null,

    @param:Json(name = "kanker_serviks")
    val kankerServiks: Int?= null,

    @param:Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int?= null,

    @param:Json(name = "bati_balita_sakit_terdata")
    val bayiBalitaSakitTerdata: Int?= null,

    @param:Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int?= null,

    @param:Json(name = "created_at")
    val createdAt: String?= null,

    @param:Json(name = "updated_at")
    val updatedAt: String?= null
)



@JsonClass(generateAdapter = true)
data class KiaReq(

    @param:Json(name = "ibu_hamil_rutin_periksa")
    val ibuHamilRutinPeriksa: Int?= null,

    @param:Json(name = "persalinan_tenaga_kesehatan")
    val persalinanTenagaKesehatan: Int?= null,

    @param:Json(name = "kematian_ibu_nifas")
    val kematianIbuNifas: Int?= null,

    @param:Json(name = "kanker_serviks")
    val kankerServiks: Int?= null,

    @param:Json(name = "imunisasi_bayi_balita")
    val imunisasiBayiBalita: Int?= null,

    @param:Json(name = "bati_balita_sakit_terdata")
    val bayiBalitaSakitTerdata: Int?= null,

    @param:Json(name = "kematian_bayi_balita")
    val kematianBayiBalita: Int?= null,
)