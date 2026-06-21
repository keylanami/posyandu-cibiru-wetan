package com.desacibiruwetan.posyandu.data.model

import com.desacibiruwetan.posyandu.data.schema.LogAktivitas
import com.desacibiruwetan.posyandu.data.schema.LogAktivitasLinks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okio.Path


@JsonClass(generateAdapter = true)
data class LogAktivitasPaginated(
    @Json(name = "current_page")
    val currentPage: Int,

    @Json(name = "data")
    val listAktivitas: List<LogAktivitas>,

    @Json(name = "first_page_url")
    val firstPageUrl: String? = null,

    val from: Int? = null,

    @Json(name = "last_page_url")
    val lastPageUrl: String? = null,

    @Json(name = "last_page")
    val lastPage: Int,

    val links: List<LogAktivitasLinks>,

    @Json(name = "next_page_url")
    val nextPageUrl: String? = null,

    @Json(name = "path")
    val path: String? = null,

    @Json(name = "per_page")
    val perPage: Int,

    @Json(name = "prev_page_url")
    val prevPageUrl: String? = null,

    val to: Int,

    val total: Int

)