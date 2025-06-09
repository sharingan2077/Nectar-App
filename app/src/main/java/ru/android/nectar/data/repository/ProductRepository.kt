package ru.android.nectar.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.api.ProductApiService
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.ProductEntity
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "ProductRepository"

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productApiService: ProductApiService,
    private val connectivityManager: ConnectivityManager,
    @ApplicationContext private val context: Context // Добавляем контекст
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


    suspend fun syncWithServer() {
        if (isOnline()) {
            try {
                val serverProducts = productApiService.getAllProducts()
                val localProducts = productDao.getAllProductsSync()

                if (serverProducts.toSet() != localProducts.toSet()) {
                    Log.d(TAG, "Data is different, refreshing products")
                    refreshProducts(serverProducts)
                } else {
                    Log.d(TAG, "Local and server products are the same, no refresh needed")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync with server", e)
                throw e
            }
        }
    }


    suspend fun uploadLocalChanges() {
        if (!isOnline()) throw IOException("No internet connection")

        val token = AuthStorage.getAuthData(context)?.token
            ?: throw IOException("Not authenticated")

        try {
            val localProducts = productDao.getAllProductsSync()
//            val remoteProducts = localProducts.map { it.toRemote() }

            val response = productApiService.syncProducts(
                authHeader = "Bearer $token",
                products = localProducts
            )

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "No error details"
                throw IOException("Server error ${response.code()}: $errorBody")
            }

            // Логируем успешный ответ
            response.body()?.let {
                Log.d("SYNC", "Server response: $it")
            }
        } catch (e: Exception) {
            Log.e("SYNC", "Upload failed", e)
            throw e
        }
    }

    private fun isOnline(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}
