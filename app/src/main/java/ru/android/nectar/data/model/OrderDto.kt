package ru.android.nectar.data.model

import ru.android.nectar.data.local.entity.OrderEntity
import ru.android.nectar.data.local.entity.OrderItemEntity

// Запрос для создания заказа
data class OrderRequest(
    val userId: Int,
    val items: List<OrderItemRequest>,
    val totalAmount: Double,
    val deliveryAddress: String?
)

data class OrderItemRequest(
    val productId: Int,
    val quantity: Int
)

// Ответ сервера с заказом
data class OrderResponse(
    val id: Int,
    val userId: Int,
    val orderDate: Long, // timestamp, проще обрабатывать
    val totalAmount: Double,
    val deliveryAddress: String?,
    val items: List<OrderItemResponse>
)

data class OrderItemResponse(
    val product: ProductResponse,
    val quantity: Int
)

// Преобразование из OrderResponse -> локальные сущности
fun OrderResponse.toOrderEntity() = OrderEntity(
    id = id,
    orderDate = orderDate,
    totalAmount = totalAmount,
    deliveryAddress = deliveryAddress
)

fun OrderItemResponse.toOrderItemEntity(orderId: Int) = OrderItemEntity(
    orderId = orderId,
    productId = product.id,
    quantity = quantity
)
