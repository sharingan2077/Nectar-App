package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.entity.FavouriteEntity

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavouriteEntity)

    @Query("DELETE FROM favorite_products WHERE userId = :userId AND productId = :productId")
    suspend fun removeFavorite(userId: Int, productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE userId = :userId AND productId = :productId)")
    fun isFavorite(userId: Int, productId: Int): Flow<Boolean>

    @Query("SELECT productId FROM favorite_products WHERE userId = :userId")
    fun getFavoriteProducts(userId: Int): Flow<List<Int>>
}