package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity


@Entity("tabel_kia")
data class KiaEntity(
    val id: Int,
    val idServer: Int,
    val ibuHamilRutinPeriksa: Int,
    val persalinanTenagaKesehatan: Int,
    val kematianIbuNifas: Int,
    val kankerServiks: Int,
    val imunisasiBayiBalita: Int,
    val batiBalitaSakitTerdata: Int,
    val kematianBayiBalita: Int,
    val createdAt: String,
    val updatedAt: String,
    val isSynced: Boolean
)
