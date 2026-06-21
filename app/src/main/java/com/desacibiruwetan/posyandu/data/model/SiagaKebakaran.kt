package com.desacibiruwetan.posyandu.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SiagaKebakaranData(

    val id: Int,

    @Json(name = "kebakaran_rumah_tangga")
    val kebakaranRumahTangga: Int?= null,

    @Json(name = "kebakaran_non_rumah_tangga")
    val kebakaranNonRumahTangga: Int?= null,

    @Json(name = "rumah_punya_apar_atau_air")
    val rumahPunyaAparAtauAir: Int?= null,

    @Json(name = "rumah_semi_permanen_kayu")
    val rumahSemiPermanenKayu: Int?= null,

    @Json(name = "rumah_punya_p3k")
    val rumahPunyaP3k: Int?= null,

    @Json(name = "kecelakaan_rumah_tangga")
    val kecelakaanRumahTangga: Int?= null,

    @Json(name = "instalasi_hydrant")
    val instalasiHydrant: Int?= null,

    @Json(name = "created_at")
    val createdAt: String?= null,

    @Json(name = "updated_at")
    val updatedAt: String?= null
)

@JsonClass(generateAdapter = true)
data class SiagaKebakaranReq(
    @Json(name = "kebakaran_rumah_tangga")
    val kebakaranRumahTangga: Int?= null,

    @Json(name = "kebakaran_non_rumah_tangga")
    val kebakaranNonRumahTangga: Int?= null,

    @Json(name = "rumah_punya_apar_atau_air")
    val rumahPunyaAparAtauAir: Int?= null,

    @Json(name = "rumah_semi_permanen_kayu")
    val rumahSemiPermanenKayu: Int?= null,

    @Json(name = "rumah_punya_p3k")
    val rumahPunyaP3k: Int?= null,

    @Json(name = "kecelakaan_rumah_tangga")
    val kecelakaanRumahTangga: Int?= null,

    @Json(name = "instalasi_hydrant")
    val instalasiHydrant: Int?= null,
)