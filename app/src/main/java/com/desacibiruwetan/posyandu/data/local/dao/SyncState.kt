package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desacibiruwetan.posyandu.data.local.entity.SyncStateEntity

@Dao
interface SyncStateDao {
    @Query("select lastSyncedAt from sync_state where entityName = :entityName limit 1")
    suspend fun getLastSyncedAt(entityName: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: SyncStateEntity)

    @Query("delete from sync_state")
    suspend fun clear()
}
