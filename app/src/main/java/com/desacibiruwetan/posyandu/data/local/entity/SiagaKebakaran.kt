package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabel_siaga_kebakaran")
data class SiagaKebakaranEntity(
    @PrimaryKey(autoGenerate = true)
    val idSiagaKebakaranLocal: Int = 0,
    val idSiagaKebakaranServer: Int? = null,
    val kebakaranRumahTangga: Int? = null,
    val kebakaranNonRumahTangga: Int? = null,
    val rumahPunyaAparAtauAir: Int? = null,
    val rumahSemiPermanenKayu: Int? = null,
    val rumahPunyaP3k: Int? = null,
    val kecelakaanRumahTangga: Int? = null,
    val instalasiHydrant: Int?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false
)
