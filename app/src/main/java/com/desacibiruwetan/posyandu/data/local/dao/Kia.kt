package com.desacibiruwetan.posyandu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.desacibiruwetan.posyandu.data.local.entity.KiaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface KiaDao {
    @Query("select * from tabel_kia order by id desc")
    fun getAllKiaDao(): Flow<List<KiaEntity>>


}