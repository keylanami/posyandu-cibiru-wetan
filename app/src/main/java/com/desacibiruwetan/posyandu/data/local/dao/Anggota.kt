package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AnggotaDao{
    @Query("select * from tabel_anggota order by localId desc")
    fun getAllAnggotaDao(): Flow<List<AnggotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnggotaLocal(anggotaEntity: AnggotaEntity): Long

    @Update
    suspend fun updateAnggotaLocal(anggota: AnggotaEntity)

    @Query("select * from tabel_anggota where isSynced = 0")
    suspend fun getAnggotaBelumSinkron(): List<AnggotaEntity>

    @Query("delete from tabel_anggota")
    suspend fun deleteAllAnggotaLocal()

    @Query("select * from tabel_anggota where keluargaId = :id order by localId desc")
    fun getAnggotaByKeluargaId(id: Int): Flow<List<AnggotaEntity>>

    @Query("select * from tabel_anggota where localId = :id or serverId = :id limit 1")
    suspend fun getAnggotaByLocalOrServerId(id: Int): AnggotaEntity?

}
