package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface Phbs {

    @Query("select * from tabel_phbs order by idPhbsLocal desc")
    fun getAllPhbsDao(): Flow<List<Phbs>>

    @Update
    suspend fun updatePhbsLocal(phbs: Phbs)

    @Query("select * from tabel_phbs where isSynced = 0")
    suspend fun getPhbsBelumSinkron(): List<Phbs>

    @Query("delete from tabel_phbs")
    suspend fun deleteAllPhbsLocal()

    @Query("select * from tabel_phbs where idPhbsLocal = :localId or idPhbsServer = :serverId limit 1")
    suspend fun getPhbsByAnggotaId(localId: Int, serverId: Int?): Phbs?
}