package ru.android.nectar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.local.entity.UserEntity
import javax.inject.Singleton

@Database(entities = [ProductEntity::class, FavouriteEntity::class, UserEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Singleton
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop_database"
                )
//                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()  // Удаляет старую БД при несовпадении версий
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
