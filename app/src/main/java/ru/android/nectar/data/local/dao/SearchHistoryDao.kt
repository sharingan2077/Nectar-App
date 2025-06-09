package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    @Transaction
    @Query("""
        SELECT p.* FROM search_history h
        JOIN products p ON h.productId = p.id
        ORDER BY h.timestamp DESC
        LIMIT 10
    """)
    suspend fun getRecentProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(item: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun clearAll()

    @Query("DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY timestamp DESC LIMIT 10)")
    suspend fun trimToLimit()
}
