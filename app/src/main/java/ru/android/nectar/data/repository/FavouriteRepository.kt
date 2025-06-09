package ru.android.nectar.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.api.ProductFavoriteApi
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.ProductEntity
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val api: ProductFavoriteApi,
    private val favouriteDao: FavouriteDao,
    private val productDao: ProductDao,
    @ApplicationContext private val context: Context
) {

    suspend fun getAllFavoritesByUserId(): List<ProductEntity> {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        return api.getFavorites(userId)
    }

    suspend fun addFavorite(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val result = api.addFavorite(userId, productId).isSuccessful
        if (result) {
            favouriteDao.addFavorite(FavouriteEntity(productId))
        }
        return result
    }

    suspend fun removeFavorite(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val result = api.removeFavorite(userId, productId).isSuccessful
        if (result) {
            favouriteDao.removeFavorite(productId)
        }
        return result
    }

    fun isFavorite(productId: Int) = favouriteDao.isFavorite(productId)

    fun getFavouriteProducts() = favouriteDao.getFavouriteProducts()

    fun getFavouriteProductIds() = favouriteDao.getFavouriteProductIds()

    fun getProductById(id: Int): Flow<ProductEntity> {
        return productDao.getProductById(id)
    }

}
