package com.desacibiruwetan.posyandu.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desacibiruwetan.posyandu.data.local.dao.AnggotaDao
import com.desacibiruwetan.posyandu.data.local.dao.BalitaDao
import com.desacibiruwetan.posyandu.data.local.dao.BumilDao
import com.desacibiruwetan.posyandu.data.local.dao.KeluargaDao
import com.desacibiruwetan.posyandu.data.local.dao.KiaDao
import com.desacibiruwetan.posyandu.data.local.dao.PeduliStuntingDao
import com.desacibiruwetan.posyandu.data.local.dao.PhbsDao
import com.desacibiruwetan.posyandu.data.local.dao.RumahDao
import com.desacibiruwetan.posyandu.data.local.dao.SiagaKebakaranDao
import com.desacibiruwetan.posyandu.data.local.dao.WusPusDao
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity

@Database(entities = [RumahEntity::class, KeluargaEntity::class, AnggotaEntity::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rumahDao(): RumahDao
    abstract fun keluargaDao(): KeluargaDao
    abstract fun anggotaDao(): AnggotaDao
    abstract fun balitaDao(): BalitaDao
    abstract fun bumilDao(): BumilDao
    abstract fun wusPusDao(): WusPusDao
    abstract fun phbsDao(): PhbsDao
    abstract fun peduliStuntingDao(): PeduliStuntingDao
    abstract fun kiaDao(): KiaDao
    abstract fun siagaKebakaranDao(): SiagaKebakaranDao
    abstract fun kbDao(): WusPusDao

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