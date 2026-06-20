package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity

@Dao
interface BumilDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBumilLocal(entitas: BumilEntity)


    @Update
    suspend fun updateBumilLocal(entitas: BumilEntity)


    @Query("select * from tabel_bumil where anggotaLocalId = :localId or anggotaServerId = :serverId limit 1")
    suspend fun getBumilByAnggotaId(localId: Int, serverId: Int?): BumilEntity?


    @Query("DELETE FROM tabel_bumil where idLocalBumil = :localId or bumilServerId = :serverId")
    suspend fun deleteAllBumil(localId: Int, serverId: Int?)

    @Query("SELECT * FROM tabel_bumil WHERE isSynced = 0")
    suspend fun getBumilBelumSync(): List<BumilEntity>

}