package com.desacibiruwetan.posyandu.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.dao.BalitaDao
import com.desacibiruwetan.posyandu.data.local.dao.BumilDao
import com.desacibiruwetan.posyandu.data.local.dao.KeluargaDao
import com.desacibiruwetan.posyandu.data.local.dao.KbDao
import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.dao.SyncStateDao
import com.desacibiruwetan.posyandu.data.local.dao.WusPusDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.KbEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.local.entity.SyncStateEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity

@Database(
    entities = [
        RumahEntity::class,
        KeluargaEntity::class,
        AnggotaEntity::class,
        BalitaEntity::class,
        BumilEntity::class,
        WusPusEntity::class,
        KbEntity::class,
        SyncStateEntity::class
    ],
    version = 13,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rumahDao(): RumahDao
    abstract fun keluargaDao(): KeluargaDao
    abstract fun anggotaDao(): AnggotaDao
    abstract fun balitaDao(): BalitaDao
    abstract fun bumilDao(): BumilDao
    abstract fun wusPusDao(): WusPusDao
    abstract fun kbDao(): KbDao
    abstract fun syncStateDao(): SyncStateDao

    suspend fun clearUserData() {
        syncStateDao().clear()
        kbDao().deleteAllKbLocal()
        wusPusDao().deleteAllWusPusLocal()
        bumilDao().deleteAllBumilLocal()
        balitaDao().deleteAllBalita()
        anggotaDao().deleteAllAnggotaLocal()
        keluargaDao().deleteAllKeluargaLocal()
        rumahDao().deleteAllRumahLocal()
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "posyandu_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
