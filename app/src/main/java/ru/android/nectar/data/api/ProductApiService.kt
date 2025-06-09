package ru.android.nectar.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.android.nectar.data.local.entity.ProductEntity

interface ProductApiService {
    @GET("products")
    suspend fun getAllProducts(): List<ProductEntity>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductEntity>

    @POST("products/sync")
    suspend fun syncProducts(
        @Header("Authorization") authHeader: String,
        @Body products: List<ProductEntity>
    ): Response<Unit>
}