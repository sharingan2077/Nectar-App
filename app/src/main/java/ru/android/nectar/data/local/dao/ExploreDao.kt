package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.entity.ProductEntity

@Dao
interface ExploreDao {
    @Query("SELECT * FROM products WHERE LOWER(name) LIKE LOWER(:query || '%')")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE LOWER(productType) = LOWER(:type)")
    fun getProductsByType(type: String): Flow<List<ProductEntity>>
}
