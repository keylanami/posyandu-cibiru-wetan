package com.desacibiruwetan.posyandu.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.dao.BalitaDao
import com.desacibiruwetan.posyandu.data.local.dao.BumilDao
import com.desacibiruwetan.posyandu.data.local.dao.KeluargaDao
import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.dao.WusPusDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BalitaEntity
import com.desacibiruwetan.posyandu.data.local.entity.BumilEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity

@Database(
    entities = [
        RumahEntity::class,
        KeluargaEntity::class,
        AnggotaEntity::class,
        BalitaEntity::class,
        BumilEntity::class,
        WusPusEntity::class
    ],
    version = 12,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rumahDao(): RumahDao
    abstract fun keluargaDao(): KeluargaDao
    abstract fun anggotaDao(): AnggotaDao
    abstract fun balitaDao(): BalitaDao
    abstract fun bumilDao(): BumilDao
    abstract fun wusPusDao(): WusPusDao

    suspend fun clearUserData() {
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
