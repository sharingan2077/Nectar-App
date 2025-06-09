package ru.android.nectar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.android.nectar.data.local.entity.CartEntity

@Dao
interface CartDao {
    // Добавляем продукт в корзину
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCart(cartEntity: CartEntity)

    // Удаляем продукт из корзины
    @Query("DELETE FROM cart_products WHERE productId = :productId")
    suspend fun removeCart(productId: Int)

    // Проверяем, есть ли продукт в корзине
    @Query("SELECT EXISTS(SELECT 1 FROM cart_products WHERE productId = :productId)")
    fun isCart(productId: Int): Flow<Boolean>

    // Получаем все продукты в корзине
    @Query("SELECT * FROM cart_products")
    fun getCartProducts(): Flow<List<CartEntity>>

    @Query("SELECT productId FROM cart_products")
    fun getCartProductIds(): Flow<List<Int>>


    // Получаем конкретный продукт из корзины
    @Query("SELECT * FROM cart_products WHERE productId = :productId")
    fun getCartProduct(productId: Int): Flow<CartEntity>

    // Увеличиваем количество
    @Query("UPDATE cart_products SET count = count + 1 WHERE productId = :productId")
    suspend fun incrementCount(productId: Int)




    @Transaction
    suspend fun decrementOrRemove(productId: Int) {
        val currentCount = getCount(productId)
        if (currentCount > 1) {
            decrementCount(productId) // Уменьшаем count
        } else {
            removeCart(productId)  // Удаляем товар
        }
    }

    // Запросы в DAO:
    @Query("UPDATE cart_products SET count = count - 1 WHERE productId = :productId AND count > 1")
    suspend fun decrementCount(productId: Int)

    @Query("SELECT count FROM cart_products WHERE productId = :productId")
    suspend fun getCount(productId: Int): Int

    // Массовое добавление (для синхронизации)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cartItems: List<CartEntity>)

    // Очищаем таблицу перед синхронизацией
    @Query("DELETE FROM cart_products")
    suspend fun clearAll()
}