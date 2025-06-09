package ru.android.nectar.data.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.android.nectar.data.local.entity.ProductEntity

interface ProductFavoriteApi {
    // Получить все избранные продукты пользователя
    @GET("favorites/{userId}")
    suspend fun getFavorites(
        @Path("userId") userId: Int
    ): List<ProductEntity>

    // Добавить продукт в избранное
    @POST("favorites/{userId}/{productId}")
    suspend fun addFavorite(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    // Удалить продукт из избранного
    @DELETE("favorites/{userId}/{productId}")
    suspend fun removeFavorite(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>
}