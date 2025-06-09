package ru.android.nectar.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.android.nectar.data.api.OrderApi
import ru.android.nectar.data.local.dao.OrderDao
import ru.android.nectar.data.local.dao.OrderWithItems
import ru.android.nectar.data.local.dao.ProductDao
import ru.android.nectar.data.local.entity.CartEntity
import ru.android.nectar.data.local.entity.OrderEntity
import ru.android.nectar.data.local.entity.OrderItemEntity
import ru.android.nectar.data.model.OrderItemRequest
import ru.android.nectar.data.model.OrderRequest
import ru.android.nectar.data.model.OrderResponse
import ru.android.nectar.data.model.toOrderEntity
import ru.android.nectar.data.model.toOrderItemEntity
import ru.android.nectar.data.model.toProductEntity
import javax.inject.Inject


private const val TAG = "OrderRepository"


class OrderRepository @Inject constructor(
    private val api: OrderApi,
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
    @ApplicationContext private val context: Context
) {
    suspend fun createOrder(
        cartItems: List<CartEntity>,
        totalAmount: Double,
        deliveryAddress: String?
    ): Long {
        Log.d(TAG, "Создание заказа...")
        Log.d(TAG, "cartItems = $cartItems, totalAmount = $totalAmount, address = $deliveryAddress")

        val userId = AuthStorage.getAuthData(context)?.userId
            ?: throw IllegalStateException("User not logged in")
        Log.d(TAG, "userId = $userId")

        if (cartItems.isEmpty()) {
            Log.e(TAG, "Корзина пуста")
            throw IllegalStateException("Корзина пуста")
        }

        val request = OrderRequest(
            userId = userId,
            items = cartItems.map {
                OrderItemRequest(
                    productId = it.productId,
                    quantity = it.count
                )
            },
            totalAmount = totalAmount,
            deliveryAddress = deliveryAddress
        )

        Log.d(TAG, "Отправка запроса на сервер: $request")
        val response = api.createOrder(request)

        Log.d(TAG, "Ответ от сервера: $response")

        return withContext(Dispatchers.IO) {
            Log.d(TAG, "Сохраняем заказ в Room")

            response.items.map { it.product }.forEach { productResponse ->
                if (productDao.getProductById(productResponse.id) == null) {
                    Log.d(TAG, "Добавление нового продукта в Room: ${productResponse.id}")
                    productDao.insertProduct(productResponse.toProductEntity())
                }
            }

            val orderId = orderDao.insertOrder(response.toOrderEntity())
            Log.d(TAG, "Заказ сохранён в Room: orderId = $orderId")

            orderDao.insertOrderItems(
                response.items.map { item ->
                    item.toOrderItemEntity(response.id).also {
                        if (productDao.getProductById(item.product.id) == null) {
                            productDao.insertProduct(item.product.toProductEntity())
                            Log.d(TAG, "Повторное добавление продукта: ${item.product.id}")
                        }
                    }
                }
            )

            orderId
        }
    }


    suspend fun getOrderHistory(): List<OrderWithItems> {
        val userId = AuthStorage.getAuthData(context)?.userId
            ?: throw IllegalStateException("User not logged in")

        return withContext(Dispatchers.IO) {
            try {
                // 1. Получаем заказы с сервера
                val remoteOrders = api.getAllOrders()

                // 2. Синхронизируем локальную БД
                syncOrdersWithLocal(remoteOrders)

                // 3. Возвращаем локальные данные
                orderDao.getAllOrders()
            } catch (e: Exception) {
                // В случае ошибки сети возвращаем локальные данные
                orderDao.getAllOrders()
            }
        }
    }

    private suspend fun syncOrdersWithLocal(remoteOrders: List<OrderResponse>) {
        // 1. Удаляем локальные заказы, которых нет на сервере
        val remoteIds = remoteOrders.map { it.id }
        val localIds = orderDao.getAllOrderIds()

        localIds.filterNot { it in remoteIds }.forEach { orderId ->
            orderDao.deleteOrderItems(orderId)
            orderDao.deleteOrder(orderId)
        }

        // 2. Обновляем/добавляем заказы
        remoteOrders.forEach { order ->
            // Сохраняем продукты
            order.items.forEach { item ->
                if (productDao.getProductById(item.product.id) == null) {
                    productDao.insertProduct(item.product.toProductEntity())
                }
            }

            // Обновляем заказ
            orderDao.insertOrder(order.toOrderEntity())
            orderDao.insertOrderItems(
                order.items.map { it.toOrderItemEntity(order.id) }
            )
        }
    }

}