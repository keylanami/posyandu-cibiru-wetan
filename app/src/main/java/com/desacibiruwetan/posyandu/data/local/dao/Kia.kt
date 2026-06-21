package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.KiaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface KiaDao {
    @Query("select * from tabel_kia order by id desc")
    fun getAllKiaDao(): Flow<List<KiaEntity>>

    @Query("select * from tabel_kia where isSynced = 0")
    suspend fun getKiaBelumSinkron(): List<KiaEntity>

    @Query("delete from tabel_kia")
    suspend fun deleteAllKiaLocal()

    @Query("select * from tabel_kia where id = :localId or idServer = :serverId limit 1")
    suspend fun getKiaByAnggotaId(localId: Int, serverId: Int): KiaEntity?

    @Update
    suspend fun updateKiaLocal(kia: KiaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKiaLocal(kia: KiaEntity): Long



}