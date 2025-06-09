package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.entity.FavouriteEntity

@Dao
interface FavouriteDao {
    // Добавляем продукт в избранное (userId теперь не нужен)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favouriteEntity: FavouriteEntity)

    // Удаляем продукт из избранного
    @Query("DELETE FROM favorite_products WHERE productId = :productId")
    suspend fun removeFavorite(productId: Int)

    // Проверяем, есть ли продукт в избранном
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE productId = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>

    // Получаем все избранные продукты
    @Query("SELECT * FROM favorite_products")
    fun getFavouriteProducts(): Flow<List<FavouriteEntity>>

    @Query("SELECT productId FROM favorite_products")
    fun getFavouriteProductIds(): Flow<List<Int>>

    // Массовое добавление (для синхронизации)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<FavouriteEntity>) // Принимаем список productId

    // Получаем все записи (для синхронизации)
    @Query("SELECT * FROM favorite_products")
    suspend fun getAll(): List<FavouriteEntity>

    // Очищаем таблицу перед синхронизацией
    @Query("DELETE FROM favorite_products")
    suspend fun clearAll()
}