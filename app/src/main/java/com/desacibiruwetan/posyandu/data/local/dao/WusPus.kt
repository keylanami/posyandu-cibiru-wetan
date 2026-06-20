package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WusPusDao {
    @Query("select * from tabel_wuspus order by idLocalWusPus desc")
    fun getAllWusPusDao(): Flow<List<WusPusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWusPusLocal(wusPusEntity: WusPusEntity): Long

    @Update
    suspend fun updateWusPusLocal(wusPus: WusPusEntity)

    @Query("select * from tabel_wuspus where isSynced = 0")
    suspend fun getWusPusBelumSinkron(): List<WusPusEntity>

    @Query("delete from tabel_wuspus")
    suspend fun deleteAllWusPusLocal()

    @Query("select * from tabel_wuspus where anggotaLocalId = :localId or anggotaServerId = :serverId limit 1")
    fun getWuspusByAnggotaId(localId: Int, serverId: Int?): WusPusEntity?
}