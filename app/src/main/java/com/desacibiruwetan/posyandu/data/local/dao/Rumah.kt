package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RumahDao{

    @Query("select * from tabel_rumah order by localId desc")
    fun getAllRumahDao(): Flow<List<RumahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRumahLocal(rumahEntity: RumahEntity): Long

    @Update
    suspend fun updateRumahLocal(rumah: RumahEntity)

    @Query("select * from tabel_rumah where isSynced = 0")
    suspend fun getRumahBelumSinkron(): List<RumahEntity>

    @Query("delete from tabel_rumah")
    suspend fun deleteAllRumahLocal()

    @Query("select * from tabel_rumah where localId = :id limit 1")
    fun getRumahById(id: Int): Flow<RumahEntity>

    @Query("select * from tabel_rumah where localId = :id or server_id = :id limit 1")
    suspend fun getRumahByLocalOrServerId(id: Int): RumahEntity?

    @Query("select * from tabel_rumah where server_id = :serverId limit 1")
    suspend fun getRumahByServerId(serverId: Int): RumahEntity?
}
