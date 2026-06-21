package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.KiaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface KiaDao {
    @Query("select * from tabel_kia order by id desc")
    fun getAllKiaDao(): Flow<List<KiaEntity>>

    @Query("select * from tabel_kia where isSynced = 0")
    fun getKiaBelumSinkron(): List<KiaEntity>

    @Query("delete from tabel_kia")
    fun deleteAllKiaLocal()

    @Query("select * from tabel_kia where id = :localId or idServer = :serverId limit 1")
    fun getKiaByAnggotaId(localId: Int, serverId: Int): KiaEntity?

    @Update
    fun updateKiaLocal(kia: KiaEntity)


}