package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity

@Dao
interface BalitaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalitaLocal(balita: BalitaEntity)

    @Update
    suspend fun updateBalitaLocal(balita: BalitaEntity)

    @Query("SELECT * FROM tabel_balita WHERE anggotaLocalId = :localId OR anggotaServerId = :serverId LIMIT 1")
    suspend fun getBalitaByAnggotaId(localId: Int, serverId: Int?): BalitaEntity?

    @Query("DELETE FROM tabel_balita")
    suspend fun deleteAllBalita()
}