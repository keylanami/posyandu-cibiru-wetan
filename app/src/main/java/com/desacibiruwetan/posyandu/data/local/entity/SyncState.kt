package com.desacibiruwetan.posyandu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_state")
data class SyncStateEntity(
    @PrimaryKey
    val entityName: String,
    val lastSyncedAt: String? = null
)
