package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desacibiruwetan.posyandu.data.local.entity.RiwayatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiwayatDao {
    @Query("SELECT * FROM tabel_riwayat ORDER BY id DESC")
    fun getAllRiwayatLocal(): Flow<List<RiwayatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRiwayatLocal(riwayatList: List<RiwayatEntity>)

    @Query("DELETE FROM tabel_riwayat")
    suspend fun deleteAllRiwayatLocal()
}