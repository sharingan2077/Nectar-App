package ru.android.nectar.data.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.android.nectar.data.local.entity.ProductEntity
import ru.android.nectar.data.model.ProductCartResponseRemote

interface ProductCartApi {

    @GET("cart/{userId}")
    suspend fun getCartProducts(
        @Path("userId") userId: Int
    ): List<ProductCartResponseRemote>

    @POST("cart/{userId}/{productId}")
    suspend fun addToCart(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    @PATCH("cart/{userId}/{productId}/decrease")
    suspend fun decreaseProductCount(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    @PATCH("cart/{userId}/{productId}/increase")
    suspend fun increaseProductCount(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    @DELETE("cart/{userId}/{productId}")
    suspend fun removeFromCart(
        @Path("userId") userId: Int,
        @Path("productId") productId: Int
    ): Response<Unit>

    @DELETE("cart/{userId}")
    suspend fun clearCart(
        @Path("userId") userId: Int
    ): Response<Unit>

}