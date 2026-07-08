package com.desacibiruwetan.posyandu.data.model

import com.desacibiruwetan.posyandu.data.schema.LogAktivitas
import com.desacibiruwetan.posyandu.data.schema.LogAktivitasLinks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class LogAktivitasPaginated(
    @param:Json(name = "current_page")
    val currentPage: Int,

    @param:Json(name = "data")
    val listAktivitas: List<LogAktivitas>,

    @param:Json(name = "first_page_url")
    val firstPageUrl: String? = null,

    val from: Int? = null,

    @param:Json(name = "last_page_url")
    val lastPageUrl: String? = null,

    @param:Json(name = "last_page")
    val lastPage: Int,

    val links: List<LogAktivitasLinks>,

    @param:Json(name = "next_page_url")
    val nextPageUrl: String? = null,

    @param:Json(name = "path")
    val path: String? = null,

    @param:Json(name = "per_page")
    val perPage: Int,

    @param:Json(name = "prev_page_url")
    val prevPageUrl: String? = null,

    val to: Int,

    val total: Int

)