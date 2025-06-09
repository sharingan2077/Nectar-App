package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.dao.SearchHistoryDao
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.local.entity.SearchHistoryEntity

class ExploreRepository(
    private val exploreDao: ExploreDao,
    private val searchHistoryDao: SearchHistoryDao
) {

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return if (query.isBlank()) {
            flowOf(emptyList())
        } else {
            exploreDao.searchProducts(query)
        }
    }

    fun getProductsByType(type: String): Flow<List<ProductEntity>> {
        return exploreDao.getProductsByType(type)
    }

    suspend fun addToSearchHistory(productId: Int) {
        searchHistoryDao.insertHistory(SearchHistoryEntity(productId = productId))
        searchHistoryDao.trimToLimit()
    }

    suspend fun getSearchHistory(): List<ProductEntity> {
        return searchHistoryDao.getRecentProducts()
    }

    suspend fun clearSearchHistory() {
        searchHistoryDao.clearAll()
    }
}