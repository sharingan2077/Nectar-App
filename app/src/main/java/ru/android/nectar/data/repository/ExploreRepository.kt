package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.android.nectar.data.local.dao.ExploreDao
import ru.android.nectar.data.local.entity.ProductEntity

class ExploreRepository(private val exploreDao: ExploreDao) {

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return if (query.isBlank()) {
            flowOf(emptyList()) // Возвращаем пустой список, если запрос пустой
        } else {
            exploreDao.searchProducts(query)
        }
    }
}
