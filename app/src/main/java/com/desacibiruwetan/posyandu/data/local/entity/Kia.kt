package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("tabel_kia")
data class KiaEntity(
    @PrimaryKey(autoGenerate = true)
    val idKiaLocal: Int =  0,
    val idKiaServer: Int?= null,
    val ibuHamilRutinPeriksa: Int?= null,
    val persalinanTenagaKesehatan: Int?= null,
    val kematianIbuNifas: Int?= null,
    val kankerServiks: Int?= null,
    val imunisasiBayiBalita: Int?= null,
    val bayiBalitaSakitTerdata: Int?= null,
    val kematianBayiBalita: Int?= null,
    val createdAt: String?= null,
    val updatedAt: String?= null,
    val isSynced: Boolean = false
)
