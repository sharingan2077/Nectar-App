package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import ru.android.nectar.data.local.entity.OrderEntity
import ru.android.nectar.data.local.entity.OrderItemEntity
import ru.android.nectar.data.local.entity.ProductEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    suspend fun getAllOrders(): List<OrderWithItems>

    // Отдельный запрос для получения продуктов для каждого элемента заказа
    @Transaction
    @Query("""
        SELECT * FROM order_items 
        WHERE orderId = :orderId
    """)
    suspend fun getOrderItemsWithProducts(orderId: Int): List<OrderItemWithProduct>

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: Int)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItems(orderId: Int)

    @Query("SELECT id FROM orders")
    suspend fun getAllOrderIds(): List<Int>
}

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId",
        entity = OrderItemEntity::class
    )
    val items: List<OrderItemWithProduct> // Теперь храним только OrderItemEntity
)

data class OrderItemWithProduct(
    @Embedded val orderItem: OrderItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)