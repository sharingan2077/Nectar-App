package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.ProductEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun isProductTableIsEmpty(): Boolean {
        return productDao.getProductCount() == 0
    }

    fun getProductsByCategory(type: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(type)
    }

    suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertAll(products)
    }

    // В твоем репозитории
    suspend fun refreshProducts(products: List<ProductEntity>) {
        // Очищаем таблицу
        productDao.clearAll()
        // Вставляем новые данные
        productDao.insertAll(products)
    }

}
