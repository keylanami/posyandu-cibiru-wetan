package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KeluargaDao{

    @Query("select * from tabel_keluarga order by localId desc")
    fun getAllKeluargaDao(): Flow<List<KeluargaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeluargaLocal(keluargaEntity: KeluargaEntity): Long

    @Update
    suspend fun updateKeluargaLocal(keluarga: KeluargaEntity)

    @Query("select * from tabel_keluarga where isSynced = 0")
    suspend fun getKeluargaBelumSinkron(): List<KeluargaEntity>

    @Query("delete from tabel_keluarga")
    suspend fun deleteAllKeluargaLocal()

    @Query("select * from tabel_keluarga where rumahId = :id order by localId desc")
    fun getKeluargaByRumahId(id: Int): Flow<List<KeluargaEntity>>

    @Query("select * from tabel_keluarga where localId = :id or serverId = :id limit 1")
    suspend fun getKeluargaByLocalOrServerId(id: Int): KeluargaEntity?

}
