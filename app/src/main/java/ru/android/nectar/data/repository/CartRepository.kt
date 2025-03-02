package ru.android.nectar.data.repository

import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.ProductEntity
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao,
    private val productDao: ProductDao
) {

    fun isCart(userId: Int, productId: Int) = cartDao.isCart(userId, productId)

    fun getCartProducts(userId: Int) = cartDao.getCartProducts(userId)

    fun getCartProduct(userId: Int, productId: Int) = cartDao.getCartProduct(userId, productId)

    fun getProductById(id: Int): Flow<ProductEntity> {
        return productDao.getProductById(id) // Получаем продукт по id
    }

    suspend fun addCart(cart: CartEntity) = cartDao.addCart(cart)

    suspend fun removeCart(userId: Int, productId: Int) = cartDao.removeCart(userId, productId)

    suspend fun incrementCount(userId: Int, productId: Int) = cartDao.incrementCount(userId, productId)

    suspend fun decrementCount(userId: Int, productId: Int) = cartDao.decrementCount(userId, productId)
}
