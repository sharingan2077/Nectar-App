package ru.android.nectar.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.android.nectar.data.api.ProductCartApi
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.model.ProductCartResponseRemote
import javax.inject.Inject

private const val TAG = "CartRepository"

class CartRepository @Inject constructor(
    private val api: ProductCartApi,
    private val cartDao: CartDao,
    private val productDao: ProductDao,
    @ApplicationContext private val context: Context
) {

    suspend fun getAllCartProductsByUserId(): List<ProductCartResponseRemote> {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        return api.getCartProducts(userId)
    }

    suspend fun addProductToCart(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        Log.d(TAG, "userId is $userId")

        val result = api.addToCart(userId, productId).isSuccessful
        Log.d(TAG, "result is $result")

        if (result) {
            val isInCart = cartDao.isCart(productId).first()
            if (isInCart) {
                cartDao.incrementCount(productId)
            } else {
                cartDao.addCart(CartEntity(productId, count = 1))
            }
        }

        return result
    }


    suspend fun decreaseProductCount(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val result = api.decreaseProductCount(userId, productId).isSuccessful
        if (result) {
            cartDao.decrementOrRemove(productId)
        }
        return result
    }

    suspend fun increaseProductCount(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val result = api.increaseProductCount(userId, productId).isSuccessful
        if (result) {
            cartDao.incrementCount(productId)
        }
        return result
    }

    suspend fun removeProductFromCart(productId: Int): Boolean {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val result = api.removeFromCart(userId, productId).isSuccessful
        if (result) {
            cartDao.removeCart(productId)
        }
        return result
    }

    fun isCart(productId: Int) = cartDao.isCart(productId)

    fun getCartProducts() = cartDao.getCartProducts()

    fun getCartProductIds() = cartDao.getCartProductIds()

    fun getCartProduct(productId: Int) = cartDao.getCartProduct(productId)

    fun getProductById(id: Int): Flow<ProductEntity> {
        return productDao.getProductById(id) // Получаем продукт по id
    }

    suspend fun clearCart() {
        val userId = AuthStorage.getAuthData(context)?.userId!!
        val response = api.clearCart(userId)

        Log.d("CartDebug", "Request to clear cart for userId=$userId")
        Log.d("CartDebug", "Response code: ${response.code()}")
        Log.d("CartDebug", "Response message: ${response.message()}")

        if (response.isSuccessful) {
            cartDao.clearAll()
            Log.d("CartDebug", "Local cart cleared successfully")
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("CartDebug", "Failed to clear cart: $errorBody")
            throw Exception("Не удалось очистить корзину на сервере: ${response.code()} ${response.message()}")
        }
    }

}
