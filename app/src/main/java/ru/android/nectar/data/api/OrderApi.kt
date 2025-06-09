package ru.android.nectar.data.api

import retrofit2.Response
import retrofit2.http.*
import ru.android.nectar.data.model.OrderRequest
import ru.android.nectar.data.model.OrderResponse

interface OrderApi {

    @GET("orders")
    suspend fun getAllOrders(): List<OrderResponse>

    @GET("orders/{id}")
    suspend fun getOrderById(
        @Path("id") id: Int
    ): OrderResponse

    @POST("orders")
    suspend fun createOrder(
        @Body order: OrderRequest
    ): OrderResponse

    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") id: Int
    ): Response<Unit>
}
