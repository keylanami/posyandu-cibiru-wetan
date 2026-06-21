package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabel_riwayat")
data class RiwayatEntity(
    @PrimaryKey
    val id: Int,
    val logName: String? = null,
    val description: String,
    val subjectType: String? = null,
    val subjectId: Int? = null,
    val event: String? = null,
    val causerType: String? = null,
    val causerId: Int? = null,

    val properties: String? = null,

    val batchUuid: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)