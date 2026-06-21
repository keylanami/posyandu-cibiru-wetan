package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("tabel_kia")
data class KiaEntity(
    @PrimaryKey(autoGenerate = true)
    val idKiaLocal: Int =  0,
    val idKiaServer: Int?= null,
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
