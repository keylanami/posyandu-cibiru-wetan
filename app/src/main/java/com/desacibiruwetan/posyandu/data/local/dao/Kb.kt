package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.KbEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface KbDao {
    @Query("select * from tabel_kb order by idKbLocal desc")
    fun getAllKbDao(): Flow<List<KbEntity>>

    @Query("select * from tabel_kb where isSynced = 0")
    fun getKbBelumSinkron(): List<KbEntity>

    @Query("delete from tabel_kb")
    fun deleteAllKbLocal()

    @Query("select * from tabel_kb where idKbLocal = :localId or idKbServer = :serverId limit 1")
    fun getKbByAnggotaId(localId: Int, serverId: Int): KbEntity?

    @Update
    fun updateKbLocal(kb: KbEntity)
}