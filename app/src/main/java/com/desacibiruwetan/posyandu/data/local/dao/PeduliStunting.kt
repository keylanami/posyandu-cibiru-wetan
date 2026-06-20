package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.PeduliStuntingEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PeduliStuntingDao {

    @Update
    suspend fun updatePeduliStuntingLocal(peduliStunting: PeduliStuntingEntity)

    @Query("select * from tabel_peduli_stunting order by idPeduliStuntingLocal desc")
    fun getAllPeduliStuntingDao(): Flow<List<PeduliStuntingEntity>>

    @Query("select * from tabel_peduli_stunting where isSynced = 0")
    suspend fun getPeduliStuntingBelumSinkron(): List<PeduliStuntingEntity>

    @Query("delete from tabel_peduli_stunting")
    suspend fun deleteAllPeduliStuntingLocal()

    @Query("select * from tabel_peduli_stunting where idPeduliStuntingLocal = :localId or idPeduliStuntingServer = :serverId limit 1")
    suspend fun getPeduliStuntingByAnggotaId(localId: Int, serverId: Int?): PeduliStuntingEntity?

}