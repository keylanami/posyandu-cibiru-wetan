package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.PhbsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PhbsDao {

    @Query("select * from tabel_phbs order by idPhbsLocal desc")
    fun getAllPhbsDao(): Flow<List<PhbsEntity>>

    @Update
    suspend fun updatePhbsLocal(phbs: PhbsEntity)

    @Query("select * from tabel_phbs where isSynced = 0")
    suspend fun getPhbsBelumSinkron(): List<PhbsEntity>

    @Query("delete from tabel_phbs")
    suspend fun deleteAllPhbsLocal()

    @Query("select * from tabel_phbs where idPhbsLocal = :localId or idPhbsServer = :serverId limit 1")
    suspend fun getPhbsByAnggotaId(localId: Int, serverId: Int?): PhbsEntity?
}