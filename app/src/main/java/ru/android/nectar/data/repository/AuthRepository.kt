package ru.android.nectar.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.android.nectar.data.api.AuthApi
import ru.android.nectar.data.api.ProductCartApi
import ru.android.nectar.data.api.ProductFavoriteApi
import ru.android.nectar.data.local.dao.CartDao
import ru.android.nectar.data.local.dao.FavouriteDao
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.FavouriteEntity
import ru.android.nectar.data.model.AuthData
import ru.android.nectar.data.model.LoginRequest
import ru.android.nectar.data.model.RegisterRequest
import ru.android.nectar.data.model.toEntity
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TAG = "AuthRepository"

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val productFavoriteApi: ProductFavoriteApi,
    private val productCartApi: ProductCartApi,
    private val favoriteDao: FavouriteDao,
    private val cartDao: CartDao,
    @ApplicationContext private val context: Context
) {


    suspend fun register(
        login: String,
        password: String,
        username: String
    ): Result<Unit> {
        return try {
            val response = api.register(RegisterRequest(login, password, username))
            AuthStorage.saveAuthData(
                context = context,
                token = response.token,
                userId = response.userId,
                username = response.username
            )
            syncUserData(response.userId) // Синхронизируем данные после регистрации
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(login: String, password: String): Result<Unit> {
        return try {
            val response = api.login(LoginRequest(login, password))
            AuthStorage.saveAuthData(
                context = context,
                token = response.token,
                userId = response.userId,
                username = response.username
            )
            syncUserData(response.userId) // Синхронизируем данные после входа
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun syncUserData(userId: Int) {
        try {
            // Синхронизация избранного
            val favorites = productFavoriteApi.getFavorites(userId)
            favoriteDao.clearAll()
            favoriteDao.insertAll(favorites.map { FavouriteEntity(it.id) }) // Предполагаем, что ProductResponseRemote имеет поле id

            // Синхронизация корзины
            val cartItems = productCartApi.getCartProducts(userId)
            cartDao.clearAll()
            cartDao.insertAll(cartItems.map {
                it.toEntity()
            })
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to sync user data", e)
        }
    }

    suspend fun logout() {
        AuthStorage.clearAuthData(context)
    }

    fun getCurrentUsername(): String? {
        return AuthStorage.getAuthData(context)?.username
    }

    fun isAuthenticated(): Boolean {
        return AuthStorage.getAuthData(context) != null
    }
}