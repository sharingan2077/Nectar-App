package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.ProductEntity
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val favouriteDao: FavouriteDao,
    private val productDao: ProductDao
) {

    fun isFavorite(userId: Int, productId: Int) = favouriteDao.isFavorite(userId, productId)

    fun getFavouriteProducts(userId: Int) = favouriteDao.getFavouriteProducts(userId)

    fun getProductById(id: Int): Flow<ProductEntity> {
        return productDao.getProductById(id) // Получаем продукт по id
    }

    suspend fun addFavorite(favorite: FavouriteEntity) = favouriteDao.addFavorite(favorite)

    suspend fun removeFavorite(userId: Int, productId: Int) = favouriteDao.removeFavorite(userId, productId)
}
