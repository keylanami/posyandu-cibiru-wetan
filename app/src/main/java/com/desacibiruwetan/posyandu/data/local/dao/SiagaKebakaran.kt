package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.desacibiruwetan.posyandu.data.local.entity.SiagaKebakaranEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SiagaKebakaranDao {

    @Query("select * from tabel_siaga_kebakaran order by idSiagaKebakaranLocal desc")
    fun getAllSiagaKebakaranDao(): Flow<List<SiagaKebakaranEntity>>

    @Update
    suspend fun updateSiagaKebakaran(siagaKebakaran: SiagaKebakaranEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiagaKebakaran(siagaKebakaran: SiagaKebakaranEntity): Long

    @Query("select * from tabel_siaga_kebakaran where isSynced = 0")
    suspend fun getSiagaKebakaranBelumSinkron(): List<SiagaKebakaranEntity>

    @Query("delete from tabel_siaga_kebakaran")
    suspend fun deleteAllSiagaKebakaranLocal()

    @Query("select * from tabel_siaga_kebakaran where idSiagaKebakaranLocal = :localId or idSiagaKebakaranServer = :serverId limit 1")
    suspend fun getSiagaKebakaranById(localId: Int, serverId: Int?): SiagaKebakaranEntity?
}