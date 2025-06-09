package ru.android.nectar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.dao.OrderDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.dao.SearchHistoryDao
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.local.entity.OrderEntity
import ru.android.nectar.data.local.entity.OrderItemEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.local.entity.SearchHistoryEntity
import javax.inject.Singleton

@Database(
    entities = [ProductEntity::class, FavouriteEntity::class, CartEntity::class, OrderEntity::class, OrderItemEntity::class, SearchHistoryEntity::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun cartDao(): CartDao
    abstract fun exploreDao(): ExploreDao
    abstract fun orderDao(): OrderDao
    abstract fun searchHistoryDao(): SearchHistoryDao

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
